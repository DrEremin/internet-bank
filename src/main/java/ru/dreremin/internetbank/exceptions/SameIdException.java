package ru.dreremin.internetbank.exceptions;

public class SameIdException extends Exception {

    public SameIdException(String message) { super(message); }

    @Override
    public String toString() {

        return "SameIdException: " + super.getMessage();
    }
}
