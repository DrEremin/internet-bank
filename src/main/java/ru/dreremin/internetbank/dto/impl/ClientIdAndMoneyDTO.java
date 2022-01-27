package ru.dreremin.internetbank.dto.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;

@Slf4j
@JsonIgnoreProperties({"isRealInputNumber"})
public class ClientIdAndMoneyDTO
        extends BankAccountDTO
        implements Serializable {

    private final BigDecimal money;

    @JsonCreator
    public ClientIdAndMoneyDTO(double userId,
                               BigDecimal money,
                               String localDate,
                               String localTime,
                               String zoneId) {

        super(userId, localDate, localTime, zoneId);
        this.money = money.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public void validation() throws IncorrectNumberException {

        try {
            if (this.isRealInputNumber) {
                throw new IncorrectNumberException(
                        "Value of user id must not be real number");
            }
            if (clientId <= 0) {
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

    public BigDecimal getMoney() { return money; }
}
