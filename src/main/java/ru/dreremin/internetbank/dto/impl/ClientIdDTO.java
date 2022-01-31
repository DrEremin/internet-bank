package ru.dreremin.internetbank.dto.impl;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;

@Slf4j
@JsonIgnoreProperties({ "isRealInputNumber" })
public class ClientIdDTO extends BankAccountDTO implements Serializable {

    @JsonCreator
    public ClientIdDTO(double userId,
                       String localDate,
                       String localTime,
                       String zoneId) {

        super(userId, localDate, localTime, zoneId);
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
        } catch (IncorrectNumberException e) {
            log.error(e.toString());
            throw e;
        }
    }
}
