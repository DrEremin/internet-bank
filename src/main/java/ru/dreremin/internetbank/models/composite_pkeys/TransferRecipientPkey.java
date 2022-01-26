package ru.dreremin.internetbank.models.composite_pkeys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TransferRecipientPkey implements Serializable {

    @Column(name = "account_id")
    private long accountId;

    @Column(name = "operation_id")
    private long operationId;

    public TransferRecipientPkey() {}

    public TransferRecipientPkey(long accountId, long operationId) {
        this.accountId = accountId;
        this.operationId = operationId;
    }

    public long getAccountId() { return accountId; }

    public long getOperationId() { return operationId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferRecipientPkey)) return false;
        TransferRecipientPkey that = (TransferRecipientPkey) o;
        return getAccountId() == that.getAccountId() && getOperationId() == that.getOperationId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountId(), getOperationId());
    }
}
