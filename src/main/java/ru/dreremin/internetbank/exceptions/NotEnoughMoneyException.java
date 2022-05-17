package ru.dreremin.internetbank.exceptions;

public class NotEnoughMoneyException extends Exception {

    public NotEnoughMoneyException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "NotEnoughMoneyException: " + super.getMessage();
    }
}
