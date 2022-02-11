package ru.dreremin.internetbank.services;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.UniquenessViolationException;
import ru.dreremin.internetbank.models.BankAccount;
import ru.dreremin.internetbank.repositories.BankAccountRepository;

@Slf4j
@Service
public class BankAccountManager {

    private final ClientService service;
    private final BankAccountRepository repository;

    public BankAccountManager(ClientService service,
                              BankAccountRepository repository) {

        this.service = service;
        this.repository = repository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = { UniquenessViolationException.class })
    public void createAccount(ClientIdDTO clientIdDTO)
            throws UniquenessViolationException, DataMissingException {

        Optional<BankAccount> optionalBankAccount =
                repository.getBankAccountByClientId(
                        clientIdDTO.getClientId());
        try {
            if (!service.
                    isClientWithThisIdExist(clientIdDTO.getClientId())) {
                throw new DataMissingException(
                        "Client with this ID not found");
            }
            if (optionalBankAccount.isPresent()) {
                throw new UniquenessViolationException(
                        "Client account with this id already exists");
            }
        } catch (DataMissingException | UniquenessViolationException e) {
            log.error(e.getMessage());
            throw e;
        }
        repository.save(new BankAccount(clientIdDTO.getClientId()));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = { DataMissingException.class })
    public void deleteAccount(ClientIdDTO clientIdDTO)
            throws DataMissingException {

        Optional<BankAccount> optionalBankAccount =
                repository.getBankAccountByClientId(
                        clientIdDTO.getClientId());

        if (optionalBankAccount.isEmpty()) {
            String message =
                    "Client with this ID or his bank account does not exist";
            log.error(message);
            throw new DataMissingException(message);
        }
        repository.delete(optionalBankAccount.get());
    }
}
