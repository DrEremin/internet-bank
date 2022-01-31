package ru.dreremin.internetbank.models;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"operationId"})
public class OperationDescription {

    @Id
    private long operationId;

    private ZonedDateTime dateTime;

    private String operationName;

    private BigDecimal transactionAmount;

    private long senderAccountId;

    private Long recipientAccountId;

    public OperationDescription() {}

    public OperationDescription(long operationId,
                                ZonedDateTime dateTime,
                                String operationName,
                                BigDecimal transactionAmount,
                                long senderAccountId,
                                Long recipientAccountId) {

        this.operationId = operationId;
        this.dateTime = dateTime;
        this.operationName = operationName;
        this.transactionAmount = transactionAmount;
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = Optional.ofNullable(recipientAccountId).orElse((long)0);
    }

    public long getOperationId() { return operationId; }

    public ZonedDateTime getDateTime() { return dateTime; }

    public String getOperationName() { return operationName; }

    public BigDecimal getTransactionAmount() { return transactionAmount; }

    public long getSenderAccountId() { return senderAccountId; }

    public long getRecipientAccountId() { return recipientAccountId; }

    public void removingNullFromRecipientAccountId() {
        this.recipientAccountId =
                Optional.ofNullable(this.recipientAccountId).orElse((long)0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationDescription)) return false;
        OperationDescription that = (OperationDescription) o;
        return getOperationId() == that.getOperationId()
                && getSenderAccountId() == that.getSenderAccountId()
                && getRecipientAccountId() == that.getRecipientAccountId()
                && getDateTime().equals(that.getDateTime())
                && getOperationName().equals(that.getOperationName())
                && getTransactionAmount().equals(that.getTransactionAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOperationId(),
                getDateTime(),
                getOperationName(),
                getTransactionAmount(),
                getSenderAccountId(),
                getRecipientAccountId());
    }

    @Override
    public String toString() {
        return String.format("DATE/TIME/ZONE: %-40s " +
                        " OPERATION TYPE: %-30s " +
                        " AMOUNT: %15s " +
                        " SENDER'S ACCOUNT ID: %-23d " +
                        " RECIPIENT'S ACCOUNT ID: %-23d",
                dateTime.toString(),
                operationName,
                transactionAmount.toString(),
                senderAccountId,
                Optional.ofNullable(recipientAccountId).orElse((long)0));
    }
}