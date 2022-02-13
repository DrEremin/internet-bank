package ru.dreremin.internetbank.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.slf4j.Slf4j;

import ru.dreremin.internetbank.exceptions.DateTimeOutOfBoundsException;

@Slf4j
public class ZonedDateTimePeriodDTO implements Serializable {

    private final ZonedDateTime startTimePoint;

    private final ZonedDateTime endTimePoint;

    private final boolean isNullableTimePoint;

    @JsonCreator
    public ZonedDateTimePeriodDTO(String startDate,
                                  String endDate,
                                  String zoneId) {

        if (Optional.ofNullable(startDate).isEmpty()
                || Optional.ofNullable(endDate).isEmpty()) {
            this.startTimePoint = null;
            this.endTimePoint = null;
            this.isNullableTimePoint = true;
        } else {
            this.startTimePoint = ZonedDateTime.of(
                    LocalDate.parse(startDate),
                    LocalTime.parse("00:00:00"),
                    ZoneId.of(zoneId));
            this.endTimePoint = ZonedDateTime.of(
                    LocalDate.parse(endDate),
                    LocalTime.parse("00:00:00"),
                    ZoneId.of(zoneId));
            this.isNullableTimePoint = false;
        }
    }

    public ZonedDateTime getStartTimePoint() {
        return this.startTimePoint;
    }

    public ZonedDateTime getEndTimePoint() {
        return this.endTimePoint;
    }

    public boolean getIsNullableTimePoint() {
        return this.isNullableTimePoint;
    }

    public void validation() throws DateTimeOutOfBoundsException {

        if (!isNullableTimePoint) {
            try {
                if (this.startTimePoint.toLocalDateTime()
                        .compareTo(this.endTimePoint.toLocalDateTime()) >= 0) {

                    throw new DateTimeOutOfBoundsException(
                            "Start point of time greater or equal than to end");
                }
            } catch (DateTimeOutOfBoundsException e) {
                log.error(e.getMessage());
                throw e;
            }
        }
    }
}