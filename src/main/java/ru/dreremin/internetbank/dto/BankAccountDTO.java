package ru.dreremin.internetbank.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;

public abstract class BankAccountDTO {

    protected final long userId;

    protected final boolean isRealInputNumber;

    protected final LocalDate localDate;

    protected final LocalTime localTime;

    protected final ZoneId zoneId;

    public BankAccountDTO (double userId,
                      String localDate,
                      String localTime,
                      String zoneId) {

        this.userId = (long) userId;
        isRealInputNumber = (this.userId - userId) != 0;
        this.localDate = LocalDate.parse(localDate);
        this.localTime = LocalTime.parse(localTime);
        this.zoneId = ZoneId.of(zoneId);
    }

    abstract public void validation() throws IncorrectNumberException;

    public long getUserId() { return userId; }

    public boolean isRealInputNumber() { return isRealInputNumber; }

    public LocalDate getLocalDate() { return localDate; }

    public LocalTime getLocalTime() { return localTime; }

    public ZoneId getZoneId() { return zoneId; }
}
