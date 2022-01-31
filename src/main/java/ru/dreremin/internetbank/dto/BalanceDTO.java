package ru.dreremin.internetbank.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BalanceDTO implements Serializable {

    private final BigDecimal balance;
    private final String status;

    public BalanceDTO(BigDecimal balance, String status) {

        this.balance = balance;
        this.status = status;
    }

    public BigDecimal getBalance() { return balance; }

    public String getStatus() { return status; }
}
