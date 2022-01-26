package ru.dreremin.internetbank.models;

import java.util.Objects;
import javax.persistence.*;
import ru.dreremin.internetbank.models.composite_pkeys.TransferRecipientPkey;

@Entity
@Table(name = "transfer_recipient")
public class TransferRecipient {

    @EmbeddedId
    private TransferRecipientPkey id;

    public TransferRecipient() {}

    public TransferRecipient(TransferRecipientPkey id) {
        this.id = id;
    }

    public TransferRecipientPkey getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferRecipient)) return false;
        TransferRecipient that = (TransferRecipient) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() { return Objects.hash(getId()); }
}
