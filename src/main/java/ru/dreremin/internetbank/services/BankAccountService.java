package ru.dreremin.internetbank.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.dto.impl.TransferMoneyDTO;
import ru.dreremin.internetbank.dto.impl.UpdatingFundsDTO;
import ru.dreremin.internetbank.enums.OperationTypes;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.NotEnoughMoneyException;
import ru.dreremin.internetbank.models.BankAccount;
import ru.dreremin.internetbank.repositories.BankAccountRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final OperationService operationService;

    private final TransferRecipientService transferRecipientService;

    private final String MESSAGE = "Client with this id does not exist";

    @Transactional(
            isolation = Isolation.SERIALIZABLE,
            rollbackFor = DataMissingException.class)
    public BigDecimal getBalance(ClientIdDTO clientIdDTO) throws
            DataMissingException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByClientId(clientIdDTO.getClientId());

        if (optionalBankAccount.isEmpty()) {
            log.error(this.MESSAGE);
            throw new DataMissingException(this.MESSAGE);
        }

        BankAccount bankAccount = optionalBankAccount.get();
        operationService.saveOperation(
                clientIdDTO,
                bankAccount.getId(),
                OperationTypes.GET_BALANCE.getValue(),
                BigDecimal.valueOf(0.0));

        return bankAccount.getCurrentBalance();
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE,
            rollbackFor = DataMissingException.class)
    public void putMoney(UpdatingFundsDTO updatingFundsDTO) throws
            DataMissingException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByClientId(updatingFundsDTO.getClientId());

        if (optionalBankAccount.isEmpty()) {
            log.error(this.MESSAGE);
            throw new DataMissingException(this.MESSAGE);
        }

        BankAccount bankAccount = optionalBankAccount.get();
        increaseBalance(
                updatingFundsDTO.getMoney(),
                bankAccount);

        operationService.saveOperation(
                updatingFundsDTO,
                bankAccount.getId(),
                OperationTypes.PUT_MONEY.getValue(),
                updatingFundsDTO.getMoney());
    }

    @Transactional(isolation =
            Isolation.SERIALIZABLE, rollbackFor =
            { DataMissingException.class, NotEnoughMoneyException.class })
    public void takeMoney(UpdatingFundsDTO updatingFundsDTO)
            throws DataMissingException, NotEnoughMoneyException {

        Optional<BankAccount> optionalBankAccount =
                bankAccountRepository.getBankAccountByClientId(
                        updatingFundsDTO.getClientId());

        if (optionalBankAccount.isEmpty()) {
            log.error(this.MESSAGE);
            throw new DataMissingException(this.MESSAGE);
        }

        BankAccount bankAccount = optionalBankAccount.get();
        reduceBalance(updatingFundsDTO.getMoney(), bankAccount);

        operationService.saveOperation(
                updatingFundsDTO,
                bankAccount.getId(),
                OperationTypes.TAKE_MONEY.getValue(),
                updatingFundsDTO.getMoney());
    }

    private void reduceBalance(BigDecimal money, BankAccount bankAccount)
            throws NotEnoughMoneyException {

        if (bankAccount.getCurrentBalance().compareTo(money) < 0) {
            String message = "There are not enough money on the account";
            log.error(message);
            throw new NotEnoughMoneyException(message);
        }

        bankAccount.setCurrentBalance(bankAccount
                .getCurrentBalance()
                .subtract(money)
                .setScale(2, RoundingMode.DOWN));
        bankAccountRepository.save(bankAccount);
    }

    private void increaseBalance(BigDecimal money, BankAccount bankAccount) {

        bankAccount.setCurrentBalance(bankAccount
                .getCurrentBalance()
                .add(money)
                .setScale(2, RoundingMode.DOWN));
        bankAccountRepository.save(bankAccount);
    }

    @Transactional(isolation =
            Isolation.SERIALIZABLE, rollbackFor =
            { DataMissingException.class, NotEnoughMoneyException.class })
    public void transferMoney(
            TransferMoneyDTO transferMoneyDTO)
            throws DataMissingException, NotEnoughMoneyException {

        Optional<BankAccount> optionalBankAccountSender =
                bankAccountRepository.getBankAccountByClientId(
                        transferMoneyDTO.getClientId());
        Optional<BankAccount> optionalBankAccountRecipient =
                bankAccountRepository.getBankAccountByClientId(
                        transferMoneyDTO.getRecipientId());
        try {
            if (optionalBankAccountSender.isEmpty()) {
                throw new DataMissingException(
                        "Sender with this id does not exist");
            }
            if (optionalBankAccountRecipient.isEmpty()) {
                throw new DataMissingException(
                        "Recipient with this id does not exist");
            }
            reduceBalance(transferMoneyDTO.getMoney(),
                    optionalBankAccountSender.get());
            increaseBalance(transferMoneyDTO.getMoney(),
                    optionalBankAccountRecipient.get());
        } catch (DataMissingException | NotEnoughMoneyException e) {
            log.error(e.getMessage());
            throw e;
        }

        long operationId = operationService.saveOperation(
                transferMoneyDTO,
                optionalBankAccountSender.get().getId(),
                OperationTypes.TRANSFER_MONEY.getValue(),
                transferMoneyDTO.getMoney());

        transferRecipientService.saveTransferRecipient(
                optionalBankAccountRecipient.get().getId(),
                operationId);
    }
}



