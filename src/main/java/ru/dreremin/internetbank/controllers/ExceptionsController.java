package ru.dreremin.internetbank.controllers;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneRulesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.dreremin.internetbank.dto.StatusOperationDTO;
import ru.dreremin.internetbank.exceptions.*;

@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler(IncorrectNumberException.class)
    public ResponseEntity<StatusOperationDTO> handleIncorrectNumberException(
            IncorrectNumberException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(-1, e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<StatusOperationDTO> handleDateTimeParseException(
            DateTimeParseException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(-1, "Incorrect date or time format"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<StatusOperationDTO> handleDateTimeException(
            DateTimeException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(-1, "Zone id has an invalid format"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataMissingException.class)
    public ResponseEntity<StatusOperationDTO> handleDataMissingException(
            DataMissingException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(0, e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ZoneRulesException.class)
    public ResponseEntity<StatusOperationDTO> handleZoneRulesException(
            ZoneRulesException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(
                        0, "Zone id is a region ID that cannot be found"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<StatusOperationDTO> handleNotEnoughMoneyException(
            NotEnoughMoneyException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(0, e.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UniquenessViolationException.class)
    public ResponseEntity<StatusOperationDTO> handleUniquenessViolationException(
            UniquenessViolationException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(0, e.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(SameIdException.class)
    public ResponseEntity<StatusOperationDTO> handleSameIdException(
            SameIdException e) {
        return new ResponseEntity<>(
                new StatusOperationDTO(0, e.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

/*
* getBalance():
*   1. Некорректный формат Id пользователя       (-1)    IncorrectNumberException  400
*   2. Некорректный формат даты|времени          (-1)      DateTimeParseException  400
*   3. Некорректный формат временной зоны        (-1)           DateTimeException  400
*   4. Временная зона не найдена                 ( 0)          ZoneRulesException  404
*   5. Нет пользователя с таким Id               ( 0)        DataMissingException  404
*      Успех:                                    ( 1)                              200
* ====================================================================================
* takeMoney():
*   1. Некорректный формат Id пользователя       (-1)    IncorrectNumberException  400
*   2. Некорректный формат суммы                 (-1)    IncorrectNumberException  400
*   3. Некорректный формат даты|времени          (-1)      DateTimeParseException  400
*   4. Некорректный формат временной зоны        (-1)           DateTimeException  400
*   5. Временная зона не найдена                 ( 0)          ZoneRulesException  404
*   6. Нет пользователя с таким Id               ( 0)        DataMissingException  404
*   7. Недостаточно средств на счете             ( 0)     NotEnoughMoneyException  422
*      Успех:                                    ( 1)                              200
* ====================================================================================
* putMoney():
*   1. Некорректный формат Id пользователя       (-1)    IncorrectNumberException  400
*   2. Некорректный формат суммы                 (-1)    IncorrectNumberException  400
*   3. Некорректный формат даты|времени          (-1)      DateTimeParseException  400
*   4. Некорректный формат временной зоны        (-1)           DateTimeException  400
*   5. Временная зона не найдена                 ( 0)          ZoneRulesException  404
*   6. Нет пользователя с таким Id               ( 0)        DataMissingException  404
*      Успех:                                    ( 1)                              200
* ====================================================================================
* transferMoney():
*   1. Некорректный формат Id отправителя        (-1)    IncorrectNumberException  400
*   2. Некорректный формат Id получателя         (-1)    IncorrectNumberException  400
*   3. Некорректный формат суммы                 (-1)    IncorrectNumberException  400
*   4. Некорректный формат даты|времени          (-1)      DateTimeParseException  400
*   5. Некорректный формат временной зоны        (-1)           DateTimeException  400
*   6. Временная зона не найдена                 ( 0)          ZoneRulesException  404
*   7. Нет отправителя с таким Id                ( 0)        DataMissingException  404
*   8. Нет получателя с таким Id                 ( 0)        DataMissingException  404
*   9. Id отправителя равно id получателя        ( 0)             SameIdException  422
*  10. Недостаточно средств на счете отправителя ( 0)     NotEnoughMoneyException  422
*      Успех:                                    ( 1)                              200
* ====================================================================================
* operationList():
* */

