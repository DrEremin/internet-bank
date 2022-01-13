package ru.dreremin.internetbank.dto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.Getter;
import ru.dreremin.internetbank.dto.BankAccountDTO;

@Getter
public class UserIdDTO implements BankAccountDTO {

    private final long userId;

    @JsonCreator
    public UserIdDTO(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean isValidInstance() {
        return userId > 0;
    }
}
