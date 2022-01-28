package ru.dreremin.internetbank.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.internetbank.exceptions.DateTimeOutOfBoundsException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
public class DateTimesOfPeriodWithZoneIdDTO implements Serializable {

    private final ZonedDateTime startZonedDateTime;
    private final ZonedDateTime endZonedDateTime;

    @JsonCreator
    public DateTimesOfPeriodWithZoneIdDTO(String startDate,
                                          String startTime,
                                          String endDate,
                                          String endTime,
                                          String zoneId) {

        this.startZonedDateTime = ZonedDateTime.of(
                LocalDate.parse(startDate),
                LocalTime.parse(startTime),
                ZoneId.of(zoneId));

        this.endZonedDateTime = ZonedDateTime.of(
                LocalDate.parse(endDate),
                LocalTime.parse(endTime),
                ZoneId.of(zoneId));
    }

    public ZonedDateTime getStartZonedDateTime() {
        return this.startZonedDateTime;
    }

    public ZonedDateTime getEndZonedDateTime() {
        return this.endZonedDateTime;
    }

    public void validation() throws DateTimeOutOfBoundsException {

        try {
            if (this.startZonedDateTime.toLocalDateTime().compareTo(
                    this.endZonedDateTime.toLocalDateTime()) >= 0) {
                throw new DateTimeOutOfBoundsException(
                        "Start point of time greater or equal than to end");
            }
        } catch (DateTimeOutOfBoundsException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}

/*
{
    "startDate" : "yyyy-mm-dd",
    "startTime" : "hh-mm-ss",
    "endDate" : "yyyy-mm-dd",
    "endTime" : "hh-mm-ss",
    "zoneId" : "UTC+-h"
}
 */