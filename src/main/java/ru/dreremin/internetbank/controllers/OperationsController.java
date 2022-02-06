package ru.dreremin.internetbank.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dreremin.internetbank.dto.DateTimesOfPeriodWithZoneIdDTO;
import ru.dreremin.internetbank.dto.OperationListDTO;
import ru.dreremin.internetbank.exceptions.DateTimeOutOfBoundsException;
import ru.dreremin.internetbank.services.OperationDescriptionService;

@Slf4j
@RestController
@RequestMapping("/bank")
public class OperationsController {

    private final OperationDescriptionService service;

    public OperationsController(OperationDescriptionService service) {

        this.service = service;
    }

    @PostMapping(value="/get-operation-list", consumes="application/json")
    public OperationListDTO getOperationList(
            @RequestBody DateTimesOfPeriodWithZoneIdDTO dto)
            throws DateTimeOutOfBoundsException {

        dto.validation();
        log.info("Getting a list of operations was completed successfully");
        return new OperationListDTO(
                service.getOperationList(dto));
    }
}
