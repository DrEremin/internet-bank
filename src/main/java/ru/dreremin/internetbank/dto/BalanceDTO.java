package ru.dreremin.internetbank.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BalanceDTO implements Serializable {

    private final BigDecimal balance;
    private final String status;
}
