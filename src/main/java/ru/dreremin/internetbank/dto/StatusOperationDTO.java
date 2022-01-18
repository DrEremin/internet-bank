package ru.dreremin.internetbank.dto;

import lombok.Data;

@Data
public class StatusOperationDTO {

    private final int status;

    private final String message;
}
