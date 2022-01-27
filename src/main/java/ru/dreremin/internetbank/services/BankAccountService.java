package ru.dreremin.internetbank.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.dreremin.internetbank.dto.impl.UserIdAndMoneyAndRecipientIdDTO;
import ru.dreremin.internetbank.dto.impl.UserIdAndMoneyDTO;
import ru.dreremin.internetbank.dto.impl.UserIdDTO;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.NotEnoughMoneyException;
import ru.dreremin.internetbank.exceptions.UniquenessViolationException;
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

    public BankAccountRepository getBankAccountRepository() {
        return bankAccountRepository;
    }

    public OperationService getOperationService() {
        return operationService;
    }

    public TransferRecipientService getTransferRecipientService() {
        return transferRecipientService;
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE,
            rollbackFor = DataMissingException.class)

    public BigDecimal getBalance(UserIdDTO userIdDTO) throws
            DataMissingException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByUserId(userIdDTO.getUserId());

        if (optionalBankAccount.isEmpty()) {
            String message = "User with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }

        BankAccount bankAccount = optionalBankAccount.get();
        operationService.saveOperation(
                userIdDTO,
                bankAccount.getId(),
                1,
                BigDecimal.valueOf(0.0));

        return bankAccount.getCurrentBalance();
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE,
            rollbackFor = DataMissingException.class)

    public void putMoney(UserIdAndMoneyDTO userIdAndMoneyDTO) throws
            DataMissingException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByUserId(userIdAndMoneyDTO.getUserId());

        if (optionalBankAccount.isEmpty()) {
            String message = "User with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }

        BankAccount bankAccount = optionalBankAccount.get();
        increaseBalance(
                userIdAndMoneyDTO.getMoney(),
                bankAccount);

        operationService.saveOperation(
                userIdAndMoneyDTO,
                bankAccount.getId(),
                2,
                userIdAndMoneyDTO.getMoney());
    }

    @Transactional(isolation =
            Isolation.SERIALIZABLE, rollbackFor =
            { DataMissingException.class, NotEnoughMoneyException.class })

    public void takeMoney(UserIdAndMoneyDTO userIdAndMoneyDTO) throws
            DataMissingException, NotEnoughMoneyException {

        Optional<BankAccount> optionalBankAccount =
                bankAccountRepository.getBankAccountByUserId(
                        userIdAndMoneyDTO.getUserId());

        if (optionalBankAccount.isEmpty()) {
            String message = "User with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }

        BankAccount bankAccount = optionalBankAccount.get();
        reduceBalance(userIdAndMoneyDTO.getMoney(), bankAccount);

        operationService.saveOperation(
                userIdAndMoneyDTO,
                bankAccount.getId(),
                3,
                userIdAndMoneyDTO.getMoney());
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
            UserIdAndMoneyAndRecipientIdDTO userIdAndMoneyAndRecipientIdDTO)
            throws DataMissingException, NotEnoughMoneyException {

        Optional<BankAccount> optionalBankAccountSender =
                bankAccountRepository.getBankAccountByUserId(
                        userIdAndMoneyAndRecipientIdDTO.getUserId());
        Optional<BankAccount> optionalBankAccountRecipient =
                bankAccountRepository.getBankAccountByUserId(
                        userIdAndMoneyAndRecipientIdDTO.getRecipientId());

        try {
            if (optionalBankAccountSender.isEmpty()) {
                throw new DataMissingException(
                        "Sender with this id does not exist");
            }
            if (optionalBankAccountRecipient.isEmpty()) {
                throw new DataMissingException(
                        "Recipient with this id does not exist");
            }
            reduceBalance(userIdAndMoneyAndRecipientIdDTO.getMoney(),
                    optionalBankAccountSender.get());
            increaseBalance(userIdAndMoneyAndRecipientIdDTO.getMoney(),
                    optionalBankAccountRecipient.get());
        } catch (DataMissingException | NotEnoughMoneyException e) {
            log.error(e.getMessage());
            throw e;
        }

        long operationId = operationService.saveOperation(
                userIdAndMoneyAndRecipientIdDTO,
                optionalBankAccountSender.get().getId(),
                5,
                userIdAndMoneyAndRecipientIdDTO.getMoney());

        transferRecipientService.saveTransferRecipient(
                optionalBankAccountRecipient.get().getId(),
                operationId);
    }

    public void createAccount(UserIdDTO userIdDTO)
            throws UniquenessViolationException {

        Optional<BankAccount> optionalBankAccount =
                bankAccountRepository.getBankAccountByUserId(
                        userIdDTO.getUserId());

        if (optionalBankAccount.isPresent()) {
            String message = "User account with this id already exists";
            log.error(message);
            throw new UniquenessViolationException(message);
        }
        bankAccountRepository.save(new BankAccount(userIdDTO.getUserId()));
    }

    public void deleteAccount(UserIdDTO userIdDTO)
            throws DataMissingException {

        Optional<BankAccount> optionalBankAccount =
                bankAccountRepository.getBankAccountByUserId(
                        userIdDTO.getUserId());

        if (optionalBankAccount.isEmpty()) {
            String message = "User with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }
        bankAccountRepository.delete(optionalBankAccount.get());
    }
}



