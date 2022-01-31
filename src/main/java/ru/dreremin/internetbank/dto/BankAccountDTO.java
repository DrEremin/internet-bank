package ru.dreremin.internetbank.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;
import ru.dreremin.internetbank.exceptions.SameIdException;

public abstract class BankAccountDTO {

    protected final long clientId;

    protected final boolean isRealInputNumber;

    protected final LocalDate localDate;

    protected final LocalTime localTime;

    protected final ZoneId zoneId;

    public BankAccountDTO (double clientId,
                           String localDate,
                           String localTime,
                           String zoneId) {

        this.clientId = (long) clientId;
        isRealInputNumber = (this.clientId - clientId) != 0;
        this.localDate = LocalDate.parse(localDate);
        this.localTime = LocalTime.parse(localTime);
        this.zoneId = ZoneId.of(zoneId);
    }

    abstract public void validation()
            throws IncorrectNumberException, SameIdException;

    public long getClientId() { return clientId; }

    public boolean getIsRealNumber() { return isRealInputNumber; }

    public LocalDate getLocalDate() { return localDate; }

    public LocalTime getLocalTime() { return localTime; }

    public ZoneId getZoneId() { return zoneId; }
}
