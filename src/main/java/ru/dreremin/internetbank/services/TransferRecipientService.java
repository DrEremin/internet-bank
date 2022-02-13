package ru.dreremin.internetbank.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.internetbank.models.TransferRecipient;
import ru.dreremin.internetbank.models.composite_pkeys.TransferRecipientPkey;
import ru.dreremin.internetbank.repositories.TransferRecipientRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferRecipientService {

    private final TransferRecipientRepository repository;

    public TransferRecipientRepository getTransferRecipientRepository() {
        return repository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveTransferRecipient(long accountRecipientId, long operationId) {

        TransferRecipient transferRecipient = new TransferRecipient(
                new TransferRecipientPkey(accountRecipientId, operationId));
        repository.save(transferRecipient);
    }
}
