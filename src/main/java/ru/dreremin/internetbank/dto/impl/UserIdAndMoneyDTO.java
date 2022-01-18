package ru.dreremin.internetbank.dto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Getter
public class UserIdAndMoneyDTO implements BankAccountDTO {

    private final long userId;

    private final BigDecimal money;

    private final boolean isRealInputNumber;

    @JsonCreator
    public UserIdAndMoneyDTO(double userId, BigDecimal money) {
        this.userId = (long) userId;
        isRealInputNumber = (this.userId - userId) != 0;
        this.money = money.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public void validation() throws IncorrectNumberException {

        try {
            if (this.isRealInputNumber) {
                throw new IncorrectNumberException(
                        "Value of user id must not be real number");
            }
            if (userId <= 0) {
                throw new IncorrectNumberException(
                        "Value of user id must not be less than 1");
            }
            if (money.compareTo(BigDecimal.valueOf(0.01)) < 0) {
                throw new IncorrectNumberException(
                        "Value of money must not be less than 0.01");
            }
        } catch (IncorrectNumberException e) {
            log.error(e.toString());
            throw e;
        }
    }
}
