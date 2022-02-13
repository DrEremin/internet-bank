package ru.dreremin.internetbank.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import javax.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "client_id")
    private long clientId;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    public BankAccount () {}

    public BankAccount (long clientId) {

        this.clientId = clientId;
        currentBalance = BigDecimal.valueOf(0.0);
    }

    public void setId(long id) { this.id = id; }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance.setScale(2, RoundingMode.DOWN);
    }

    /*public long getId() { return id; }

    public long getClientId() { return clientId; }

    public BigDecimal getCurrentBalance() { return currentBalance; }*/

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof BankAccount)) return false;
        BankAccount that = (BankAccount) o;
        return getId() == that.getId()
                && getClientId() == that.getClientId()
                && getCurrentBalance().equals(that.getCurrentBalance());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
