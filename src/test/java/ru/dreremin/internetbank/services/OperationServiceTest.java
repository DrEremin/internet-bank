package ru.dreremin.internetbank.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.models.Operation;
import ru.dreremin.internetbank.repositories.OperationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperationServiceTest {

    @InjectMocks
    private OperationService service;

    @Mock
    private OperationRepository repository;

    private BankAccountDTO dto;

    private Operation standard;

    long ID = 2;

    long BANK_ACCOUNT_ID = 3;

    int OPERATION_TYPE = 4;

    BigDecimal TRANSACTION_AMOUNT = BigDecimal.valueOf(100.55);

    @BeforeAll
    void testInit() {

        String date = "2020-05-01", time = "12:00:00", zone = "Europe/Moscow";
        ZonedDateTime zonedDateTime = ZonedDateTime.of(
                LocalDate.parse(date),
                LocalTime.parse(time),
                ZoneId.of(zone));

        this.dto =  new ClientIdDTO(5.0, date, time, zone);

        this.standard = new Operation(
                this.ID,
                this.BANK_ACCOUNT_ID,
                this.OPERATION_TYPE,
                zonedDateTime,
                this.TRANSACTION_AMOUNT);
    }

    @Test
    void testSaveOperation_WhenAllArgsIsCorrect() {


        ArgumentCaptor<Operation> captor =
                ArgumentCaptor.forClass(Operation.class);

        when(this.repository.save(any(Operation.class)))
                .thenReturn(this.standard);
        this.service.saveOperation(
                this.dto,
                this.BANK_ACCOUNT_ID,
                this.OPERATION_TYPE,
                this.TRANSACTION_AMOUNT);
        verify(this.repository, times(1)).save(captor.capture());

        Operation operation = captor.getValue();

        operation.setId(this.ID);
        assertEquals(this.standard, operation);
    }
}