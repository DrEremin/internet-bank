package ru.dreremin.internetbank.models;

import javax.persistence.*;
import ru.dreremin.internetbank.models.composite_pkeys.TransferRecipientPkey;

import java.util.Objects;

@Entity
@Table(name = "transfer_recipient")
public class TransferRecipient {

    @EmbeddedId
    private TransferRecipientPkey id;

    public TransferRecipient() {}

    public TransferRecipient(TransferRecipientPkey id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof TransferRecipient)) return false;
        TransferRecipient that = (TransferRecipient) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
