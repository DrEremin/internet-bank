package ru.dreremin.internetbank.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    public BankAccount () {}

    public BankAccount (long user_id) {
        this.userId = user_id;
        currentBalance = BigDecimal.valueOf(0.0);
    }

    public void setId(long id) { this.id = id; }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance.setScale(2, RoundingMode.DOWN);
    }

    public long getId() { return id; }

    public long getUserId() { return userId; }

    public BigDecimal getCurrentBalance() { return currentBalance; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount)) return false;
        BankAccount that = (BankAccount) o;
        return getId() == that.getId() && getUserId() == that.getUserId()
                && Objects.equals(getCurrentBalance(),
                that.getCurrentBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
