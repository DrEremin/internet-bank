package ru.dreremin.internetbank.services;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.models.Operation;
import ru.dreremin.internetbank.repositories.OperationRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class OperationService {

    private final OperationRepository repository;

    public OperationRepository getOperationRepository() {
        return repository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public long saveOperation(BankAccountDTO dto,
                              long bankAccountID,
                              int operationType,
                              BigDecimal transactionAmount) {

        Operation operation = new Operation(
                bankAccountID,
                operationType,
                transactionAmount);

        operation.setTimeAndDateOfOperation(
                dto.getLocalDate(),
                dto.getLocalTime(),
                dto.getZoneId());

        repository.save(operation);
        return operation.getId();
    }
}
