package ru.dreremin.internetbank.controllers;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dreremin.internetbank.dto.*;
import ru.dreremin.internetbank.dto.impl.*;
import ru.dreremin.internetbank.exceptions.*;
import ru.dreremin.internetbank.services.BankAccountService;
import ru.dreremin.internetbank.services.OperationDescriptionService;

@Slf4j
@RestController
@RequestMapping("/bank")
public class InternetBankController {

    private final BankAccountService bankAccountService;

    private final OperationDescriptionService operationDescriptionService;

    public InternetBankController(
            BankAccountService bankAccountService,
            OperationDescriptionService operationDescriptionService) {

        this.bankAccountService = bankAccountService;
        this.operationDescriptionService = operationDescriptionService;
    }

    @PostMapping(value="/get-balance", consumes="application/json")
    public BalanceDTO getBalance (@RequestBody ClientIdDTO dto)
            throws IncorrectNumberException, DataMissingException {

        dto.validation();
        BigDecimal balance = bankAccountService.getBalance(dto);
        log.info("Get balance operation was completed successfully");
        return new BalanceDTO(balance, "Ok");
    }

    @PatchMapping(value="/put-money", consumes="application/json")
    public StatusOperationDTO putMoney(@RequestBody ClientIdAndMoneyDTO dto)
            throws IncorrectNumberException, DataMissingException {

        dto.validation();
        bankAccountService.putMoney(dto);
        log.info("Put money operation was completed successfully");

        return new StatusOperationDTO(1, "Ok");
    }

    @PatchMapping(value="/take-money", consumes="application/json")
    public StatusOperationDTO takeMoney(@RequestBody ClientIdAndMoneyDTO dto)
            throws IncorrectNumberException, DataMissingException,
            NotEnoughMoneyException {

        dto.validation();
        bankAccountService.takeMoney(dto);
        log.info("Take money operation was completed successfully");
        return new StatusOperationDTO(1, "Ok");
    }

    @PatchMapping(value="/transfer-money", consumes="application/json")
    public StatusOperationDTO transferMoney(
            @RequestBody SenderIdAndMoneyAndRecipientIdDTO dto)
            throws IncorrectNumberException, DataMissingException,
            NotEnoughMoneyException, SameIdException {

        dto.validation();
        bankAccountService.transferMoney(dto);
        log.info("Transfer of money operation was completed successfully");
        return new StatusOperationDTO(1, "Ok");
    }

    @PostMapping(value="/get-operation-list", consumes="application/json")
    public OperationListDTO getOperationList(
            @RequestBody DateTimesOfPeriodWithZoneIdDTO dto)
            throws DateTimeOutOfBoundsException {

        dto.validation();
        log.info("Getting a list of operations was completed successfully");
        return new OperationListDTO(
                operationDescriptionService.getOperationList(dto));
    }

    @PutMapping(value="/create-account", consumes="application/json")
    public StatusOperationDTO createAccount(@RequestBody ClientIdDTO dto)
            throws IncorrectNumberException,
            UniquenessViolationException, DataMissingException {

        dto.validation();
        bankAccountService.createAccount(dto);
        log.info("Create account operation was completed successfully");
        return new StatusOperationDTO(1, "Ok");
    }

    @DeleteMapping(value="/delete-account", consumes="application/json")
    public StatusOperationDTO deleteAccount(@RequestBody ClientIdDTO dto)
            throws IncorrectNumberException, DataMissingException {

        dto.validation();
        bankAccountService.deleteAccount(dto);
        log.info("Delete account operation was completed successfully");
        return new StatusOperationDTO(1, "Ok");
    }
}
