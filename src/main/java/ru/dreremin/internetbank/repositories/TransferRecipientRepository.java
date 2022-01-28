package ru.dreremin.internetbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dreremin.internetbank.models.TransferRecipient;
import ru.dreremin.internetbank.models.composite_pkeys.TransferRecipientPkey;

@Repository
public interface TransferRecipientRepository
        extends JpaRepository<TransferRecipient, TransferRecipientPkey> {
}
