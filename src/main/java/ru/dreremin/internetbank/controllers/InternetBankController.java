package ru.dreremin.internetbank.controllers;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.dreremin.internetbank.dto.BalanceDTO;
import ru.dreremin.internetbank.dto.StatusOperationDTO;
import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.dto.impl.SenderIdAndMoneyAndRecipientIdDTO;
import ru.dreremin.internetbank.dto.impl.ClientIdAndMoneyDTO;
import ru.dreremin.internetbank.exceptions.*;
import ru.dreremin.internetbank.services.BankAccountService;

@Slf4j
@RestController
@RequestMapping("/bank")
public class InternetBankController {

    private final BankAccountService bankAccountService;

    public InternetBankController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping(value="/get-balance", consumes="application/json")
    public BalanceDTO getBalance (
            @RequestBody ClientIdDTO userIdDTO)
            throws IncorrectNumberException, DataMissingException {

        userIdDTO.validation();
        BigDecimal balance = bankAccountService.getBalance(userIdDTO);
        log.info("Get balance operation was completed successfully");

        return new BalanceDTO(balance, "Ok");
    }

    @PatchMapping(value="/put-money", consumes="application/json")
    public StatusOperationDTO putMoney(
            @RequestBody ClientIdAndMoneyDTO userIdAndMoneyDTO)
            throws IncorrectNumberException, DataMissingException {

        userIdAndMoneyDTO.validation();
        bankAccountService.putMoney(userIdAndMoneyDTO);
        log.info("Put money operation was completed successfully");

        return new StatusOperationDTO(1, "Ok");
    }

    @PatchMapping(value="/take-money", consumes="application/json")
    public StatusOperationDTO takeMoney(
            @RequestBody ClientIdAndMoneyDTO userIdAndMoneyDTO) throws
            IncorrectNumberException,
            DataMissingException,
            NotEnoughMoneyException {

        userIdAndMoneyDTO.validation();
        bankAccountService.takeMoney(userIdAndMoneyDTO);
        log.info("Take money operation was completed successfully");

        return new StatusOperationDTO(1, "Ok");
    }

    @PatchMapping(value="/transfer-money", consumes="application/json")
    public StatusOperationDTO transferMoney(
            @RequestBody SenderIdAndMoneyAndRecipientIdDTO
                    senderIdAndMoneyAndRecipientIdDTO) throws
            IncorrectNumberException,
            DataMissingException,
            NotEnoughMoneyException,
            SameIdException {

        senderIdAndMoneyAndRecipientIdDTO.validation();
        bankAccountService.transferMoney(senderIdAndMoneyAndRecipientIdDTO);
        log.info("Transfer of money operation was completed successfully");
        return new StatusOperationDTO(1, "Ok");
    }

    @PutMapping(value="/create-account", consumes="application/json")
    public StatusOperationDTO createAccount(@RequestBody ClientIdDTO userIdDTO)
            throws IncorrectNumberException, UniquenessViolationException {
        userIdDTO.validation();
        bankAccountService.createAccount(userIdDTO);
        log.info("Create account operation was completed successfully");
        return new StatusOperationDTO(1, "Ok");
    }

    @DeleteMapping(value="/delete-account", consumes="application/json")
    public StatusOperationDTO deleteAccount(@RequestBody ClientIdDTO userIdDTO)
            throws IncorrectNumberException, DataMissingException{
        userIdDTO.validation();
        bankAccountService.deleteAccount(userIdDTO);
        log.info("Delete account operation was completed successfully");
        return new StatusOperationDTO(1, "Ok");
    }
}
