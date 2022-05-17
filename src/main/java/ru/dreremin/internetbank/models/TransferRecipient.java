package ru.dreremin.internetbank.models;

import java.util.Objects;

import javax.persistence.*;
import lombok.AllArgsConstructor;

import ru.dreremin.internetbank.models.composite_pkeys.TransferRecipientPkey;

@AllArgsConstructor
@Entity
@Table(name = "transfer_recipient")
public class TransferRecipient {

    @EmbeddedId
    private TransferRecipientPkey id;

    public TransferRecipient() {
        this(null);
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
