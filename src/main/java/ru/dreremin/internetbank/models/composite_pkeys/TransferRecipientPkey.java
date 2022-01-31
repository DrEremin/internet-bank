package ru.dreremin.internetbank.models.composite_pkeys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TransferRecipientPkey implements Serializable {

    @Column(name = "recipient_account_id")
    private long recipientAccountId;

    @Column(name = "operation_id")
    private long operationId;

    public TransferRecipientPkey() {}

    public TransferRecipientPkey(long recipientAccountId, long operationId) {

        this.recipientAccountId = recipientAccountId;
        this.operationId = operationId;
    }

    public long getRecipientAccountId() { return recipientAccountId; }

    public long getOperationId() { return operationId; }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof TransferRecipientPkey)) return false;
        TransferRecipientPkey that = (TransferRecipientPkey) o;
        return getRecipientAccountId() == that.getRecipientAccountId()
                && getOperationId() == that.getOperationId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecipientAccountId(), getOperationId());
    }
}
