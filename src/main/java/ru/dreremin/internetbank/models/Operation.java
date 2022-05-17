package ru.dreremin.internetbank.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.*;
import lombok.Getter;

@Getter
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
    private ZonedDateTime dateTimeOfOperation;

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
                     ZonedDateTime dateTimeOfOperation,
                     BigDecimal amountOfOperation) {

        this.accountId = accountId;
        this.operationTypeId = operationTypeId;
        this.dateTimeOfOperation = dateTimeOfOperation;
        this.amountOfOperation = amountOfOperation;
    }

    public Operation(long id,
                     long accountId,
                     int operationTypeId,
                     ZonedDateTime dateTimeOfOperation,
                     BigDecimal amountOfOperation) {

        this.id = id;
        this.accountId = accountId;
        this.operationTypeId = operationTypeId;
        this.dateTimeOfOperation = dateTimeOfOperation;
        this.amountOfOperation = amountOfOperation;
    }

    public void setTimeAndDateOfOperation(LocalDate localDate,
                                          LocalTime localTime,
                                          ZoneId zoneId) {

        this.dateTimeOfOperation = ZonedDateTime.of(
                localDate, localTime, zoneId);
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Operation)) return false;
        Operation operation = (Operation) o;
        return getId() == operation.getId()
                && getAccountId() == operation.getAccountId()
                && getOperationTypeId() == operation.getOperationTypeId()
                && Objects.equals(getDateTimeOfOperation(),
                        operation.getDateTimeOfOperation())
                && Objects.equals(getAmountOfOperation(),
                        operation.getAmountOfOperation());
    }

    @Override
    public int hashCode() { return Objects.hash(getId()); }
}
