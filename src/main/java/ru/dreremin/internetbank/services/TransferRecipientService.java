package ru.dreremin.internetbank.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dreremin.internetbank.models.TransferRecipient;
import ru.dreremin.internetbank.models.composite_pkeys.TransferRecipientPkey;
import ru.dreremin.internetbank.repositories.TransferRecipientRepository;

@Slf4j
@Service
public class TransferRecipientService {

    private final TransferRecipientRepository transferRecipientRepository;

    public TransferRecipientService(
            TransferRecipientRepository transferRecipientRepository) {
        this.transferRecipientRepository = transferRecipientRepository;
    }

    public TransferRecipientRepository getTransferRecipientRepository() {

        return transferRecipientRepository;
    }

    public void saveTransferRecipient(long accountOfRecipientId, long operationId) {

        TransferRecipient transferRecipient = new TransferRecipient(
                new TransferRecipientPkey(accountOfRecipientId, operationId));
        transferRecipientRepository.save(transferRecipient);
    }
}
