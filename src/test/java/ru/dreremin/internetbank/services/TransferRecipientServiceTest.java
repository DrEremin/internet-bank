package ru.dreremin.internetbank.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import ru.dreremin.internetbank.models.TransferRecipient;
import ru.dreremin.internetbank.models.composite_pkeys.TransferRecipientPkey;
import ru.dreremin.internetbank.repositories.TransferRecipientRepository;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferRecipientServiceTest {

    @Mock
    private TransferRecipientRepository repository;

    @InjectMocks
    private TransferRecipientService service;

    Instant time;

    @BeforeEach
    void beforeEach() {
        time = Instant.now();
    }

    @AfterEach
    void afterEach() {
        log.info("run time: " + Duration.between(time, Instant.now()));
    }

    @Test
    void testSaveTransferRecipient_WhenBothArgsIsCorrect() {

        long accountRecipientId = 12;
        long operationId = 121;

        TransferRecipient standard = new TransferRecipient(
                new TransferRecipientPkey(accountRecipientId, operationId));

        ArgumentCaptor<TransferRecipient> captor =
                ArgumentCaptor.forClass(TransferRecipient.class);

        service.saveTransferRecipient(accountRecipientId, operationId);

        verify(repository, times(1)).save(captor.capture());

        assertEquals(standard, captor.getValue());
    }
}