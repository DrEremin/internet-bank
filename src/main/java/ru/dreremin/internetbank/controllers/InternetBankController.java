package ru.dreremin.internetbank.controllers;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.dreremin.internetbank.dto.BalanceDTO;
import ru.dreremin.internetbank.dto.StatusOperationDTO;
import ru.dreremin.internetbank.dto.impl.UserIdAndMoneyDTO;
import ru.dreremin.internetbank.dto.impl.UserIdDTO;
import ru.dreremin.internetbank.services.BankAccountService;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/bank")
@AllArgsConstructor
@Getter
@Setter
public class InternetBankController {

    private BankAccountService bankAccountService;

    @PostMapping(value="/get-balance", consumes="application/json")
    public BalanceDTO getBalance(
            @RequestBody UserIdDTO userIdDTO) {

        return (userIdDTO.isValidInstance())
                ? new BalanceDTO(bankAccountService.getBalance(userIdDTO))
                : new BalanceDTO(BigDecimal.valueOf(-1));
    }

    @PatchMapping(value="/put-money", consumes="application/json")
    public StatusOperationDTO putMoney(
            @RequestBody UserIdAndMoneyDTO userIdAndMoneyDTO) {

        return (userIdAndMoneyDTO.isValidInstance())
                ? new StatusOperationDTO(
                bankAccountService.putMoney(userIdAndMoneyDTO))
                : new StatusOperationDTO(0);
    }

    @PatchMapping(value="/take-money", consumes="application/json")
    public StatusOperationDTO takeMoney(
            @RequestBody UserIdAndMoneyDTO userIdAndMoneyDTO) {
        return (userIdAndMoneyDTO.isValidInstance())
                ? new StatusOperationDTO(
                bankAccountService.takeMoney(userIdAndMoneyDTO))
                : new StatusOperationDTO(0);
    }
}
