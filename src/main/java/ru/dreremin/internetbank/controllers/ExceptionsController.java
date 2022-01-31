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

    @ExceptionHandler(DateTimeOutOfBoundsException.class)
    public ResponseEntity<StatusOperationDTO> handleDateTimeOutOfBoundsException(
            DateTimeOutOfBoundsException e) {

        return new ResponseEntity<>(
                new StatusOperationDTO(0, e.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

