package ru.dreremin.internetbank.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "operation")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "account_id")
    private long accountId;

    @Column(name = "operation_type_id")
    private int operationTypeId;

    @Column(name = "date_time")
    private ZonedDateTime timeAndDateOfOperation;

    @Column(name = "transaction_amount")
    private BigDecimal amountOfOperation;

    public Operation() {}

    public Operation(long accountId,
                     int operationTypeId,
                     BigDecimal amountOfOperation) {

        this.accountId = accountId;
        this.operationTypeId = operationTypeId;
        this.amountOfOperation = amountOfOperation;
    }

    public Operation(long accountId,
                     int operationTypeId,
                     ZonedDateTime timeAndDateOfOperation,
                     BigDecimal amountOfOperation) {

        this.accountId = accountId;
        this.operationTypeId = operationTypeId;
        this.timeAndDateOfOperation = timeAndDateOfOperation;
        this.amountOfOperation = amountOfOperation;
    }

    public void setTimeAndDateOfOperation(LocalDate localDate,
                                          LocalTime localTime,
                                          ZoneId zoneId) {

        this.timeAndDateOfOperation = ZonedDateTime.of(
                localDate, localTime, zoneId);
    }

    public long getId() { return id; }

    public long getAccountId() { return accountId; }

    public int getOperationTypeId() { return operationTypeId; }

    public ZonedDateTime getTimeAndDateOfOperation() {
        return timeAndDateOfOperation;
    }

    public BigDecimal getAmountOfOperation() {
        return amountOfOperation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operation)) return false;
        Operation operation = (Operation) o;
        return getId() == operation.getId()
                && getAccountId() == operation.getAccountId()
                && getOperationTypeId() == operation.getOperationTypeId()
                && Objects.equals(getTimeAndDateOfOperation(),
                        operation.getTimeAndDateOfOperation())
                && Objects.equals(getAmountOfOperation(),
                        operation.getAmountOfOperation());
    }

    @Override
    public int hashCode() { return Objects.hash(getId()); }
}
