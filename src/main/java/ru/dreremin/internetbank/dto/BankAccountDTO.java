package ru.dreremin.internetbank.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ru.dreremin.internetbank.exceptions.IncorrectNumberException;
import ru.dreremin.internetbank.exceptions.SameIdException;
import ru.dreremin.internetbank.util.CustomLocalDateSerializer;
import ru.dreremin.internetbank.util.CustomLocalTimeSerializer;
import ru.dreremin.internetbank.util.CustomZoneIdSerializer;

public abstract class BankAccountDTO {

    protected long clientId;

    protected final boolean isRealInputNumber;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    protected final LocalDate localDate;

    @JsonSerialize(using = CustomLocalTimeSerializer.class)
    protected final LocalTime localTime;

    @JsonSerialize(using = CustomZoneIdSerializer.class)
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

    public LocalDate getLocalDate() { return localDate; }

    public LocalTime getLocalTime() { return localTime; }

    public ZoneId getZoneId() { return zoneId; }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
