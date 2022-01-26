package ru.dreremin.internetbank.dto;

import java.io.Serializable;

public class StatusOperationDTO implements Serializable {

    private final int status;
    private final String message;

    public StatusOperationDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() { return status; }

    public String getMessage() { return message; }
}
