package ru.dreremin.internetbank.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.dreremin.internetbank.dto.ZonedDateTimePeriodDTO;
import ru.dreremin.internetbank.dto.OperationListDTO;
import ru.dreremin.internetbank.exceptions.DateTimeOutOfBoundsException;
import ru.dreremin.internetbank.services.OperationDescriptionService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class OperationsController {

    private final OperationDescriptionService service;

    @PostMapping(value="/get-operation-list", consumes="application/json")
    public OperationListDTO getOperationList(
            @RequestBody ZonedDateTimePeriodDTO dto)
            throws DateTimeOutOfBoundsException {

        dto.validation();
        log.info("Getting a list of operations was completed successfully");
        return new OperationListDTO(
                service.getOperationList(dto));
    }
}
