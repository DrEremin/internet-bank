package ru.dreremin.internetbank.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "user_id")
    private long userId;

    @NonNull
    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    public BankAccount () {}

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance.setScale(2, RoundingMode.DOWN);
    }
}
