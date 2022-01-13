package ru.dreremin.internetbank.services;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;
import java.math.RoundingMode;

import ru.dreremin.internetbank.dto.impl.UserIdAndMoneyDTO;
import ru.dreremin.internetbank.dto.impl.UserIdDTO;
import ru.dreremin.internetbank.models.BankAccount;
import ru.dreremin.internetbank.repositories.BankAccountRepository;

@Slf4j
@Service
@AllArgsConstructor
@Getter
@Setter
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

    public BigDecimal getBalance(UserIdDTO userIdDTO) {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByUserId(userIdDTO.getUserId());
        return optionalBankAccount
                .map(BankAccount::getCurrentBalance)
                .orElseGet(() -> BigDecimal.valueOf(-1));
    }

    public int putMoney(UserIdAndMoneyDTO userIdAndMoneyDTO) {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByUserId(userIdAndMoneyDTO.getUserId());

        if (optionalBankAccount.isPresent()) {
            increaseBalance(
                    userIdAndMoneyDTO.getMoney(),
                    optionalBankAccount.get());
            return 1;
        }
        return 0;
    }

    public int takeMoney(UserIdAndMoneyDTO userIdAndMoneyDTO) {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository
                .getBankAccountByUserId(userIdAndMoneyDTO.getUserId());

        return optionalBankAccount.map(
                bankAccount -> reduceBalance(
                        userIdAndMoneyDTO.getMoney(),
                        bankAccount)).orElse(0);
    }

    public int reduceBalance(BigDecimal money, BankAccount bankAccount) {
        if (bankAccount.getCurrentBalance().compareTo(money) < 0) {
            return 0;
        }
        bankAccount.setCurrentBalance(bankAccount
                .getCurrentBalance()
                .subtract(money)
                .setScale(2, RoundingMode.DOWN));
        bankAccountRepository.save(bankAccount);
        return 1;
    }

    public void increaseBalance(BigDecimal money, BankAccount bankAccount) {
        bankAccount.setCurrentBalance(bankAccount
                .getCurrentBalance()
                .add(money)
                .setScale(2, RoundingMode.DOWN));
        bankAccountRepository.save(bankAccount);
    }
}



