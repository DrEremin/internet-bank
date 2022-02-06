package ru.dreremin.internetbank.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.dto.impl.SenderIdAndMoneyAndRecipientIdDTO;
import ru.dreremin.internetbank.dto.impl.ClientIdAndMoneyDTO;
import ru.dreremin.internetbank.enums.OperationTypes;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.NotEnoughMoneyException;
import ru.dreremin.internetbank.models.BankAccount;
import ru.dreremin.internetbank.repositories.BankAccountRepository;

@Slf4j
@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final OperationService operationService;

    private final TransferRecipientService transferRecipientService;

    public BankAccountService(
            BankAccountRepository bankAccountRepository,
            OperationService operationService,
            TransferRecipientService transferRecipientService) {

        this.bankAccountRepository = bankAccountRepository;
        this.operationService = operationService;
        this.transferRecipientService = transferRecipientService;
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE,
            rollbackFor = DataMissingException.class)
    public BigDecimal getBalance(ClientIdDTO clientIdDTO) throws
            DataMissingException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByClientId(clientIdDTO.getClientId());

        if (optionalBankAccount.isEmpty()) {
            String message = "Client with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
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
    public void putMoney(ClientIdAndMoneyDTO clientIdAndMoneyDTO) throws
            DataMissingException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByClientId(clientIdAndMoneyDTO.getClientId());

        if (optionalBankAccount.isEmpty()) {
            String message = "Client with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }

        BankAccount bankAccount = optionalBankAccount.get();
        increaseBalance(
                clientIdAndMoneyDTO.getMoney(),
                bankAccount);

        operationService.saveOperation(
                clientIdAndMoneyDTO,
                bankAccount.getId(),
                OperationTypes.PUT_MONEY.getValue(),
                clientIdAndMoneyDTO.getMoney());
    }

    @Transactional(isolation =
            Isolation.SERIALIZABLE, rollbackFor =
            { DataMissingException.class, NotEnoughMoneyException.class })
    public void takeMoney(ClientIdAndMoneyDTO clientIdAndMoneyDTO)
            throws DataMissingException, NotEnoughMoneyException {

        Optional<BankAccount> optionalBankAccount =
                bankAccountRepository.getBankAccountByClientId(
                        clientIdAndMoneyDTO.getClientId());

        if (optionalBankAccount.isEmpty()) {
            String message = "Client with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }

        BankAccount bankAccount = optionalBankAccount.get();
        reduceBalance(clientIdAndMoneyDTO.getMoney(), bankAccount);

        operationService.saveOperation(
                clientIdAndMoneyDTO,
                bankAccount.getId(),
                OperationTypes.TAKE_MONEY.getValue(),
                clientIdAndMoneyDTO.getMoney());
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
            SenderIdAndMoneyAndRecipientIdDTO senderIdAndMoneyAndRecipientIdDTO)
            throws DataMissingException, NotEnoughMoneyException {

        Optional<BankAccount> optionalBankAccountSender =
                bankAccountRepository.getBankAccountByClientId(
                        senderIdAndMoneyAndRecipientIdDTO.getClientId());
        Optional<BankAccount> optionalBankAccountRecipient =
                bankAccountRepository.getBankAccountByClientId(
                        senderIdAndMoneyAndRecipientIdDTO.getRecipientId());

        try {
            if (optionalBankAccountSender.isEmpty()) {
                throw new DataMissingException(
                        "Sender with this id does not exist");
            }
            if (optionalBankAccountRecipient.isEmpty()) {
                throw new DataMissingException(
                        "Recipient with this id does not exist");
            }
            reduceBalance(senderIdAndMoneyAndRecipientIdDTO.getMoney(),
                    optionalBankAccountSender.get());
            increaseBalance(senderIdAndMoneyAndRecipientIdDTO.getMoney(),
                    optionalBankAccountRecipient.get());
        } catch (DataMissingException | NotEnoughMoneyException e) {
            log.error(e.getMessage());
            throw e;
        }

        long operationId = operationService.saveOperation(
                senderIdAndMoneyAndRecipientIdDTO,
                optionalBankAccountSender.get().getId(),
                OperationTypes.TRANSFER_MONEY.getValue(),
                senderIdAndMoneyAndRecipientIdDTO.getMoney());

        transferRecipientService.saveTransferRecipient(
                optionalBankAccountRecipient.get().getId(),
                operationId);
    }
}



