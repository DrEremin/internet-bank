package ru.dreremin.internetbank.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
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
}
