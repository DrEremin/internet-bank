package ru.dreremin.internetbank.models;

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
}
