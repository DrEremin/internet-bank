package ru.dreremin.internetbank.services;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.dreremin.internetbank.models.Client;
import ru.dreremin.internetbank.repositories.ClientRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository repository;

    public Optional<Client> getClientById(long id) {
        return (repository.existsById(id))
                ? Optional.of(repository.getById(id))
                : Optional.empty();
    }

    public boolean isClientWithThisIdExist(long id) {
        return repository.existsById(id);
    }
}
