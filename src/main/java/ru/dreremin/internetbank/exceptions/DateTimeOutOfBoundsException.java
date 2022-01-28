package ru.dreremin.internetbank.exceptions;

public class DateTimeOutOfBoundsException extends Exception {

    public DateTimeOutOfBoundsException(String message) {
        super(message);
    }

    @Override
    public String toString() {

        return "DateTimeOutOfBoundsException: " + super.getMessage();
    }
}
