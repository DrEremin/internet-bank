package ru.dreremin.internetbank.controllers;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.dreremin.internetbank.dto.*;
import ru.dreremin.internetbank.dto.impl.*;
import ru.dreremin.internetbank.exceptions.*;
import ru.dreremin.internetbank.services.BankAccountService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bank")
public class InternetBankController {

    private final BankAccountService bankAccountService;

    @PostMapping(value="/get-balance", consumes="application/json")
    public BalanceDTO getBalance (@RequestBody ClientIdDTO dto)
            throws IncorrectNumberException, DataMissingException {

        dto.validation();
        BigDecimal balance = this.bankAccountService.getBalance(dto);
        log.info(String.format(
                "Get balance operation for client with ID = %d" +
                        " in the was completed successfully",
                dto.getClientId()));
        return new BalanceDTO(balance, "Ok");
    }

    @PatchMapping(value="/put-money", consumes="application/json")
    public StatusOperationDTO putMoney(@RequestBody UpdatingFundsDTO dto)
            throws IncorrectNumberException, DataMissingException {

        dto.validation();
        this.bankAccountService.putMoney(dto);
        log.info(String.format(
                "Put money operation to client with ID = %d in the " +
                        "amount of %s rubles was completed successfully",
                dto.getClientId(),
                dto.getMoney().toString()));
        return new StatusOperationDTO(1, "Ok");
    }

    @PatchMapping(value="/take-money", consumes="application/json")
    public StatusOperationDTO takeMoney(@RequestBody UpdatingFundsDTO dto)
            throws IncorrectNumberException, DataMissingException,
            NotEnoughMoneyException {

        dto.validation();
        this.bankAccountService.takeMoney(dto);
        log.info(String.format(
                "Take money operation to client with ID = %d in the " +
                        "amount of %s rubles was completed successfully",
                dto.getClientId(),
                dto.getMoney().toString()));
        return new StatusOperationDTO(1, "Ok");
    }

    @PatchMapping(value="/transfer-money", consumes="application/json")
    public StatusOperationDTO transferMoney(
            @RequestBody TransferMoneyDTO dto)
            throws IncorrectNumberException, DataMissingException,
            NotEnoughMoneyException, SameIdException {

        dto.validation();
        this.bankAccountService.transferMoney(dto);
        log.info(String.format(
                "Transfer money operation from client with ID = %d" +
                        " to client with ID = %d in the amount" +
                        " of %s rubles was completed successfully",
                dto.getClientId(),
                dto.getRecipientId(),
                dto.getMoney().toString()));
        return new StatusOperationDTO(1, "Ok");
    }
}
