package ru.dreremin.internetbank.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.UniquenessViolationException;
import ru.dreremin.internetbank.models.BankAccount;
import ru.dreremin.internetbank.repositories.BankAccountRepository;
import ru.dreremin.internetbank.repositories.ClientRepository;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankAccountManagerTest {

    @Autowired
    private BankAccountManager manager;

    @MockBean
    private ClientService service;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @MockBean
    private ClientRepository clientRepository;

    private Instant time;

    private ClientIdDTO dto;

    private BankAccount bankAccount;

    @BeforeAll
    void beforeAll() {

        this.dto = new ClientIdDTO(
                0,
                "2022-01-30",
                "12:30:35",
                "UTC+03");

        this.bankAccount = new BankAccount(0);
        this.bankAccount.setId(0);

        when(this.bankAccountRepository.save(any(BankAccount.class)))
                .thenReturn(this.bankAccount);
    }

    @BeforeEach
    void beforeEach() {
        this.time = Instant.now();
    }

    @AfterEach
    void afterEach() {
        log.info("run time: " + Duration.between(time, Instant.now()));
    }

    @Test
    void testCreateAccount_IfClientWithThisIdDoesNotExist() {

        long value = 0;

        when(this.service.isClientWithThisIdExist(value)).thenReturn(false);
        when(this.bankAccountRepository.getBankAccountByClientId(value))
                .thenReturn(Optional.empty());

        this.dto.setClientId(value);

        assertThrowsExactly(DataMissingException.class,
                ()->this.manager.createAccount(this.dto));

    }

    @Test
    void testCreateAccount_IfBankAccountWithThisIdAlreadyExist() {

        long value = 1;

        when(this.service.isClientWithThisIdExist(value)).thenReturn(true);
        when(this.bankAccountRepository.getBankAccountByClientId(value))
                .thenReturn(Optional.of(this.bankAccount));

        this.dto.setClientId(value);

        assertThrowsExactly(UniquenessViolationException.class,
                ()->this.manager.createAccount(this.dto));

    }

    @Test
    void testCreateAccount_IfExceptionsNotThrown() {

        long value = 2;

        when(this.service.isClientWithThisIdExist(value)).thenReturn(true);
        when(this.bankAccountRepository.getBankAccountByClientId(value))
                .thenReturn(Optional.empty());

        this.dto.setClientId(value);

        assertDoesNotThrow(()->this.manager.createAccount(this.dto));

        verify(this.bankAccountRepository,
                times(1)).save(any(BankAccount.class));
    }

    @Test
    void testDeleteAccount_IfClientWithThisIdDoesNotExist() {

        long value = 4;

        when(this.bankAccountRepository.getBankAccountByClientId(value))
                .thenReturn(Optional.empty());

        this.dto.setClientId(value);

        assertThrowsExactly(DataMissingException.class,
                () -> manager.deleteAccount(this.dto));
    }

    @Test
    void testDeleteAccount_IfExceptionNotThrown() {

        long value = 5;

        when(this.bankAccountRepository.getBankAccountByClientId(value))
                .thenReturn(Optional.of(this.bankAccount));

        this.dto.setClientId(value);

        assertDoesNotThrow(() -> manager.deleteAccount(this.dto));

        verify(this.bankAccountRepository,
                times(1)).delete(any(BankAccount.class));
    }
}