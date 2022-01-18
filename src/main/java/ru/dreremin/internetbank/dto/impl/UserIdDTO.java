package ru.dreremin.internetbank.dto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;

@Slf4j
@Getter
@JsonIgnoreProperties({"isRealInputNumber"})
public class UserIdDTO implements BankAccountDTO {

    private final long userId;

    private final boolean isRealInputNumber;

    @JsonCreator
    public UserIdDTO (double userId) {

        this.userId = (long) userId;
        isRealInputNumber = (this.userId - userId) != 0;
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
        } catch (IncorrectNumberException e) {
            log.error(e.toString());
            throw e;
        }
    }
}
