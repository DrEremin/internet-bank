package ru.dreremin.internetbank.dto;

import ru.dreremin.internetbank.exceptions.IncorrectNumberException;

public interface BankAccountDTO {

    void validation() throws IncorrectNumberException, Throwable;
}
