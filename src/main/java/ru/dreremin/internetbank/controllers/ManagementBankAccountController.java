package ru.dreremin.internetbank.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.dreremin.internetbank.dto.StatusOperationDTO;
import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;
import ru.dreremin.internetbank.exceptions.UniquenessViolationException;
import ru.dreremin.internetbank.services.BankAccountManager;

@Slf4j
@RestController
@RequestMapping("/account-management")
public class ManagementBankAccountController {

    private final BankAccountManager manager;

    public ManagementBankAccountController(BankAccountManager manager) {

        this.manager = manager;
    }

    @PutMapping(value="/create", consumes="application/json")
    public StatusOperationDTO createAccount(@RequestBody ClientIdDTO dto)
            throws IncorrectNumberException,
            UniquenessViolationException, DataMissingException {

        dto.validation();
        manager.createAccount(dto);
        log.info("Create account operation was completed successfully");
        return new StatusOperationDTO(1, "Ok");
    }

    @DeleteMapping(value="/delete", consumes="application/json")
    public StatusOperationDTO deleteAccount(@RequestBody ClientIdDTO dto)
            throws IncorrectNumberException, DataMissingException {

        dto.validation();
        manager.deleteAccount(dto);
        log.info("Delete account operation was completed successfully");
        return new StatusOperationDTO(1, "Ok");
    }
}
