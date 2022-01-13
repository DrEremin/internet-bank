package ru.dreremin.internetbank.dto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import ru.dreremin.internetbank.dto.BankAccountDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class UserIdAndMoneyDTO implements BankAccountDTO {

    private final long userId;

    private final BigDecimal money;

    @JsonCreator
    public UserIdAndMoneyDTO(long userId, BigDecimal money) {
        this.userId = userId;
        this.money = money.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public boolean isValidInstance() {
        return (userId > 0) && (money.compareTo(BigDecimal.valueOf(0.0)) > 0);
    }
}
