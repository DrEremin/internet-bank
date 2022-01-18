package ru.dreremin.internetbank.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDTO {

    private final BigDecimal balance;

    private final String status;
}
