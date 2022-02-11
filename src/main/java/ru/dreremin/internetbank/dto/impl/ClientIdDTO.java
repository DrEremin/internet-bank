package ru.dreremin.internetbank.dto.impl;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;

@Slf4j
public class ClientIdDTO extends BankAccountDTO implements Serializable {

    @JsonCreator
    public ClientIdDTO(@JsonProperty("clientId") double clientId,
                       @JsonProperty("localDate") String localDate,
                       @JsonProperty("localTime") String localTime,
                       @JsonProperty("zoneId") String zoneId) {

        super(clientId, localDate, localTime, zoneId);
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
