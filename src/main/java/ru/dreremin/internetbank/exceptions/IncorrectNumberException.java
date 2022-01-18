package ru.dreremin.internetbank.exceptions;

public class IncorrectNumberException extends Exception {

    public IncorrectNumberException(String message) {
        super(message);
    }

    @Override
    public String toString() {

        return "IncorrectNumberException: " + super.getMessage();
    }
}
