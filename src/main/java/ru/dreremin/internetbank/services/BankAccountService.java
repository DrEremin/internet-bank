package ru.dreremin.internetbank.services;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;
import java.math.RoundingMode;

import ru.dreremin.internetbank.dto.impl.UserIdAndMoneyDTO;
import ru.dreremin.internetbank.dto.impl.UserIdDTO;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.NotEnoughMoneyException;
import ru.dreremin.internetbank.exceptions.UniquenessViolationException;
import ru.dreremin.internetbank.models.BankAccount;
import ru.dreremin.internetbank.repositories.BankAccountRepository;

@Slf4j
@Service
@AllArgsConstructor
@Getter
@Setter
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

    public BigDecimal getBalance(UserIdDTO userIdDTO)
            throws DataMissingException {

        Optional<BigDecimal> balance = bankAccountRepository
                .getBankAccountByUserId(userIdDTO.getUserId())
                .map(BankAccount::getCurrentBalance);

        if (balance.isEmpty()) {
            String message = "User with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }

        return balance.get();
    }

    public void putMoney(UserIdAndMoneyDTO userIdAndMoneyDTO)
            throws DataMissingException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByUserId(userIdAndMoneyDTO.getUserId());

        if (optionalBankAccount.isEmpty()) {
            String message = "User with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }

        increaseBalance(
                userIdAndMoneyDTO.getMoney(),
                optionalBankAccount.get());
    }

    public void takeMoney(UserIdAndMoneyDTO userIdAndMoneyDTO)
            throws DataMissingException, NotEnoughMoneyException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByUserId(userIdAndMoneyDTO.getUserId());
        if (optionalBankAccount.isEmpty()) {
            String message = "User with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }

        reduceBalance(userIdAndMoneyDTO.getMoney(), optionalBankAccount.get());
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

    public void createAccount(UserIdDTO userIdDTO)
            throws UniquenessViolationException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByUserId(userIdDTO.getUserId());

        if (optionalBankAccount.isPresent()) {
            String message = "User account with this id already exists";
            log.error(message);
            throw new UniquenessViolationException(message);
        }
        bankAccountRepository.save(new BankAccount(userIdDTO.getUserId()));
    }

    public void deleteAccount(UserIdDTO userIdDTO)
            throws DataMissingException {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByUserId(userIdDTO.getUserId());

        if (optionalBankAccount.isEmpty()) {
            String message = "User with this id does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }
        bankAccountRepository.delete(optionalBankAccount.get());
    }
}



