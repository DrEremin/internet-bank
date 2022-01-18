package ru.dreremin.internetbank.exceptions;

public class DataMissingException extends Exception {

    public DataMissingException(String message) {
        super(message);
    }

    @Override
    public String toString() {

        return "DataMissingException: " + super.getMessage();
    }
}
