package ru.dreremin.internetbank.services;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.models.Operation;
import ru.dreremin.internetbank.repositories.OperationRepository;

@Slf4j
@Service
public class OperationService {

    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public OperationRepository getOperationRepository() {
        return operationRepository;
    }

    public long saveOperation(BankAccountDTO bankAccountDTO,
                              long bankAccountID,
                              int operationType,
                              BigDecimal transactionAmount) {

        Operation operation = new Operation(
                bankAccountID,
                operationType,
                transactionAmount);

        operation.setTimeAndDateOfOperation(
                bankAccountDTO.getLocalDate(),
                bankAccountDTO.getLocalTime(),
                bankAccountDTO.getZoneId());

        operationRepository.save(operation);
        return operation.getId();
    }
}
