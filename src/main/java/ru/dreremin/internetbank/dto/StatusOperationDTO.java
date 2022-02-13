package ru.dreremin.internetbank.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class StatusOperationDTO implements Serializable {

    private final int status;

    private final String message;
}
