package ru.dreremin.internetbank.services;

import org.springframework.stereotype.Service;
import ru.dreremin.internetbank.models.OperationDescription;
import ru.dreremin.internetbank.repositories.OperationDescriptionRepository;

import java.util.List;

@Service
public class OperationDescriptionService {

    private final OperationDescriptionRepository repository;

    public OperationDescriptionService(OperationDescriptionRepository repository) {
        this.repository = repository;
    }

    public List<OperationDescription> getOperationList() {

        return repository.getAll();
    }
}
