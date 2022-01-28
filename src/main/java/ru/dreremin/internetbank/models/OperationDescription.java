package ru.dreremin.internetbank.models;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.*;

@Entity
//@Table(name = "operation_description")
public class OperationDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Column(name = "operation_name")
    private String operationName;

    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;

    @Column(name = "account_id")
    private long accountId;

    @Nullable
    @Column(name = "recipient_account_id")
    private Long recipientAccountId;

    public OperationDescription() {}

    public OperationDescription(long id,
                                ZonedDateTime dateTime,
                                String operationName,
                                BigDecimal transactionAmount,
                                long accountId,
                                Long recipientAccountId) {
        this.id = id;
        this.dateTime = dateTime;
        this.operationName = operationName;
        this.transactionAmount = transactionAmount;
        this.accountId = accountId;
        this.recipientAccountId = Optional.ofNullable(recipientAccountId).orElse((long)0);
    }

    public long getId() { return id; }

    public ZonedDateTime getDateTime() { return dateTime; }

    public String getOperationName() { return operationName; }

    public BigDecimal getTransactionAmount() { return transactionAmount; }

    public long getAccountId() { return accountId; }

    public long getRecipientAccountId() { return recipientAccountId; }

   /* public void removingNullFromRecipientAccountId() {
        this.recipientAccountId = Optional.ofNullable(this.recipientAccountId).orElse((long)0);
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationDescription)) return false;
        OperationDescription that = (OperationDescription) o;
        return getId() == that.getId()
                && getAccountId() == that.getAccountId()
                && getRecipientAccountId() == that.getRecipientAccountId()
                && getDateTime().equals(that.getDateTime())
                && getOperationName().equals(that.getOperationName())
                && getTransactionAmount().equals(that.getTransactionAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getDateTime(),
                getOperationName(),
                getTransactionAmount(),
                getAccountId(),
                getRecipientAccountId());
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[")
                .append(id)
                .append("\t")
                .append(dateTime)
                .append("\t")
                .append(operationName)
                .append("\t")
                .append(transactionAmount)
                .append("\t")
                .append(accountId)
                .append("\t")
                .append(recipientAccountId)
                .append("]")
                .toString();
    }
}