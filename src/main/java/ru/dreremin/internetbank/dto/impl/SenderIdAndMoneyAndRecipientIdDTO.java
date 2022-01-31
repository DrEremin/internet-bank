package ru.dreremin.internetbank.dto.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;
import ru.dreremin.internetbank.exceptions.SameIdException;

@Slf4j
@JsonIgnoreProperties(
        { "isRealInputNumber", "isRealInputNumberOfRecipientId" })
public class SenderIdAndMoneyAndRecipientIdDTO
        extends BankAccountDTO implements Serializable {

    private final long recipientId;
    private final BigDecimal money;
    private final boolean isRealInputNumberOfRecipientId;

    @JsonCreator
    public SenderIdAndMoneyAndRecipientIdDTO(double userId,
                                             double recipientId,
                                             BigDecimal money,
                                             String localDate,
                                             String localTime,
                                             String zoneId) {

        super(userId, localDate, localTime, zoneId);
        this.money = money.setScale(2, RoundingMode.DOWN);
        this.recipientId = (long) recipientId;
        this.isRealInputNumberOfRecipientId =
                (this.recipientId - recipientId) != 0;
    }

    @Override
    public void validation() throws IncorrectNumberException, SameIdException {

        try {
            if (this.isRealInputNumber
                    || this.isRealInputNumberOfRecipientId) {
                throw new IncorrectNumberException(
                        "Value of user id or recipient " +
                                "id must not be real number");
            }
            if (clientId <= 0 || recipientId <= 0) {
                throw new IncorrectNumberException(
                        "Value of user id or recipient " +
                                "id must not be less than 1");
            }
            if (money.compareTo(BigDecimal.valueOf(0.01)) < 0) {
                throw new IncorrectNumberException(
                        "Value of money must not be less than 0.01");
            }
            if (clientId == recipientId) {
                throw new SameIdException(
                        "The sender's id is equal to the recipient's id");
            }
        } catch (IncorrectNumberException | SameIdException e) {
            log.error(e.toString());
            throw e;
        }
    }

    public long getRecipientId() { return recipientId; }

    public BigDecimal getMoney() { return money; }

}
