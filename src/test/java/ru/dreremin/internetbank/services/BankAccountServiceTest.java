package ru.dreremin.internetbank.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.dreremin.internetbank.dto.BankAccountDTO;
import ru.dreremin.internetbank.dto.impl.ClientIdAndMoneyDTO;
import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.dto.impl.SenderIdAndMoneyAndRecipientIdDTO;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.NotEnoughMoneyException;
import ru.dreremin.internetbank.models.BankAccount;
import ru.dreremin.internetbank.repositories.BankAccountRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankAccountServiceTest {

    @InjectMocks
    private BankAccountService service;

    @Mock
    private BankAccountRepository repository;

    @Mock
    private OperationService operationService;

    @Mock
    private TransferRecipientService transferRecipientService;

    @Mock
    private ClientService clientService;

    private ClientIdDTO clientIdDTO;

    private ClientIdAndMoneyDTO clientIdAndMoneyDTO;

    private SenderIdAndMoneyAndRecipientIdDTO senderIdAndMoneyAndRecipientIdDTO;

    private BankAccount bankAccount;

    private Instant time;

    private BankAccount copyBankAccount() {
        BankAccount bankAccount =
                new BankAccount(this.bankAccount.getClientId());
        bankAccount.setId(this.bankAccount.getId());
        bankAccount.setCurrentBalance(this.bankAccount.getCurrentBalance());
        return bankAccount;
    }

    @BeforeAll
    void beforeAll() {

        this.clientIdDTO = new ClientIdDTO(
                105,
                "2022-01-30",
                "12:30:35", "UTC+03");

        this.clientIdAndMoneyDTO = new ClientIdAndMoneyDTO(
                105,
                BigDecimal.valueOf(5),
                "2022-01-30",
                "12:30:35", "UTC+03");

        this.bankAccount = new BankAccount(105);
        this.bankAccount.setId(1);
        this.bankAccount.setCurrentBalance(BigDecimal.valueOf(50));


        when(this.operationService.saveOperation(
                any(ClientIdDTO.class),
                anyLong(),
                anyInt(),
                any(BigDecimal.class)))
                .thenReturn((long)10);
    }


    @BeforeEach
    void beforeEach() {
        time = Instant.now();
    }

    @AfterEach
    void afterEach() {
        log.info("run time: " + Duration.between(time, Instant.now()));
    }

    @Test
    void testGetBalance_WhenClientWithThisIdDoesNotExist() {

        when(this.repository.getBankAccountByClientId(anyLong()))
                .thenReturn(Optional.empty());

        assertThrowsExactly(DataMissingException.class,
                ()->this.service.getBalance(clientIdDTO));
    }

    @Test
    void testGetBalance_WhenClientWithThisIdIsExist() throws DataMissingException {

        when(this.repository.getBankAccountByClientId(anyLong()))
                .thenReturn(Optional.of(this.bankAccount));

        assertDoesNotThrow(()->this.service.getBalance(this.clientIdDTO));
        assertEquals(0, this.service.getBalance(this.clientIdDTO)
                .compareTo(BigDecimal.valueOf(50)));
    }

    @Test
    void testPutMoney_WhenClientWithThisIdDoesNotExist() {

        when(this.repository.getBankAccountByClientId(anyLong()))
                .thenReturn(Optional.empty());

        assertThrowsExactly(DataMissingException.class,
                ()->this.service.putMoney(this.clientIdAndMoneyDTO));
    }

    @Test
    void testPutMoney_WhenClientWithThisIdIsExist() {

        BankAccount bankAccount = copyBankAccount();
        when(this.repository.getBankAccountByClientId(anyLong()))
                .thenReturn(Optional.of(bankAccount));

        assertDoesNotThrow(()->this.service
                .putMoney(this.clientIdAndMoneyDTO));

        assertEquals(0, bankAccount.getCurrentBalance()
                .compareTo(BigDecimal.valueOf(55)));
    }

    @Test
    void testTakeMoney_WhenClientWithThisIdDoesNotExist() {

        when(this.repository.getBankAccountByClientId(anyLong()))
                .thenReturn(Optional.empty());

        assertThrowsExactly(DataMissingException.class,
                ()->this.service.takeMoney(this.clientIdAndMoneyDTO));
    }

    @Test
    void testTakeMoney_WhenClientWithThisIdIsExistAndIsEnoughMoney() {

        BankAccount bankAccount = copyBankAccount();
        when(this.repository.getBankAccountByClientId(anyLong()))
                .thenReturn(Optional.of(bankAccount));

        assertDoesNotThrow(()->this.service
                .takeMoney(this.clientIdAndMoneyDTO));

        assertEquals(0, bankAccount.getCurrentBalance()
                .compareTo(BigDecimal.valueOf(45)));
    }

    @Test
    void testTakeMoney_WhenClientWithThisIdIsExistAndIsNotEnoughMoney() {

        BankAccount bankAccount = copyBankAccount();
        bankAccount.setCurrentBalance(BigDecimal.valueOf(1.0));

        when(this.repository.getBankAccountByClientId(anyLong()))
                .thenReturn(Optional.of(bankAccount));

        assertThrowsExactly(NotEnoughMoneyException.class,
                ()->this.service.takeMoney(this.clientIdAndMoneyDTO));

        assertEquals(0, bankAccount.getCurrentBalance()
                .compareTo(BigDecimal.valueOf(1.0)));
    }
}