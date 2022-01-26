package ru.dreremin.internetbank.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.models.Operation;
import ru.dreremin.internetbank.repositories.OperationRepository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@Service
public class OperationService {

    private OperationRepository operationRepository;

    public void saveOperation(BankAccountDTO bankAccountDTO,
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
    }

}
