package ru.dreremin.internetbank.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.dreremin.internetbank.dto.StatusOperationDTO;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;
import ru.dreremin.internetbank.exceptions.NotEnoughMoneyException;
import ru.dreremin.internetbank.exceptions.UniquenessViolationException;

@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler(DataMissingException.class)
    public ResponseEntity<StatusOperationDTO> handleDataMissingException(
            DataMissingException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(0, e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectNumberException.class)
    public ResponseEntity<StatusOperationDTO> handleIncorrectNumberException(
            IncorrectNumberException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(-1, e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<StatusOperationDTO> handleNotEnoughMoneyException(
            NotEnoughMoneyException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(0, e.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UniquenessViolationException.class)
    public ResponseEntity<StatusOperationDTO> handleNotEnoughMoneyException(
            UniquenessViolationException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(0, e.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

/*
* getBalance():
*       1. Некорректный Id пользователя  (-1) - IncorrectNumberException       400
*       2. Нет пользователя с таким Id   (0) -  DataMissingException           404
* Успех: (1)                                                                   200
* =================================================================================
* takeMoney():
*       1. Некорректный Id пользователя  (-1) - IncorrectNumberException       400
*       2. Некорректная сумма            (-1) - IncorrectNumberException       400
*       3. Нет пользователя с таким Id   (0) -  DataMissingException           404
*       4. Недостаточно средств на счете (0)  - NotEnoughMoneyException        422
* Успех: (1)                                                                   200
* =================================================================================
* putMoney():
*       1. Некорректный Id пользователя  (-1) - IncorrectNumberException       400
*       2. Некорректная сумма            (-1) - IncorrectNumberException       400
*       3. Нет пользователя с таким Id   (0) -  DataMissingException           404
* Успех: (1)                                                                   200
* =================================================================================
*
*
*
*
* */
