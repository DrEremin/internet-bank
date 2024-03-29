package ru.dreremin.internetbank.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import ru.dreremin.internetbank.dto.impl.UpdatingFundsDTO;
import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.dto.impl.TransferMoneyDTO;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.NotEnoughMoneyException;
import ru.dreremin.internetbank.models.BankAccount;
import ru.dreremin.internetbank.repositories.BankAccountRepository;

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
    TransferRecipientService transferRecipientService;

    private ClientIdDTO clientIdDTO;

    private UpdatingFundsDTO updatingFundsDTO;

    private TransferMoneyDTO transferMoneyDTO;

    private BankAccount bankAccount;

    private BankAccount bankAccountRecipient;

    private Instant time;

    private BigDecimal balance;

    private enum Ids {

        EMPTY(1),
        BANK_ACCOUNT(2),
        BANK_ACCOUNT_RECIPIENT(3);

        private long id;

        Ids(long id) {
            this.id = id;
        }

        private long getId() {
            return id;
        }
    }

    @BeforeAll
    void beforeAll() {

        this.balance = BigDecimal.valueOf(50);
        String date = "2022-01-30";
        String time = "12:30:35";
        String zone = "UTC+03";

        this.clientIdDTO = new ClientIdDTO(
                Ids.EMPTY.getId(),
                date,
                time,
                zone);

        this.updatingFundsDTO = new UpdatingFundsDTO(
                Ids.EMPTY.getId(),
                BigDecimal.valueOf(5),
                date,
                time,
                zone);

        this.transferMoneyDTO =
                new TransferMoneyDTO(
                        Ids.EMPTY.getId(),
                        Ids.EMPTY.getId(),
                        BigDecimal.valueOf(15),
                        date,
                        time,
                        zone);

        this.bankAccount = new BankAccount(Ids.EMPTY.getId());
        this.bankAccount.setId(Ids.EMPTY.getId());
        this.bankAccount.setCurrentBalance(BigDecimal.valueOf(50));
        this.bankAccountRecipient = new BankAccount(Ids.EMPTY.getId());
        this.bankAccountRecipient.setId(Ids.BANK_ACCOUNT.getId());
        this.bankAccountRecipient.setCurrentBalance(BigDecimal.valueOf(200));

        when(this.repository.getBankAccountByClientId((long)1))
                .thenReturn(Optional.empty());
        when(this.repository.getBankAccountByClientId((long)2))
                .thenReturn(Optional.of(this.bankAccount));
        when(this.repository.getBankAccountByClientId((long)3))
                .thenReturn(Optional.of(this.bankAccountRecipient));
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
        this.bankAccount.setCurrentBalance(this.balance);
        log.info("run time: " + Duration.between(time, Instant.now()));
    }

    @Test
    void testGetBalance_WhenClientWithThisIdDoesNotExist() throws DataMissingException{

        this.clientIdDTO.setClientId(Ids.EMPTY.getId());
        assertThrowsExactly(DataMissingException.class,
                ()->this.service.getBalance(clientIdDTO));
    }

    @Test
    void testGetBalance_WhenClientWithThisIdIsExist() throws DataMissingException {

        this.clientIdDTO.setClientId(Ids.BANK_ACCOUNT.getId());
        assertDoesNotThrow(()->this.service.getBalance(this.clientIdDTO));
        assertEquals(0, this.service.getBalance(this.clientIdDTO)
                .compareTo(BigDecimal.valueOf(50)));
    }

    @Test
    void testPutMoney_WhenClientWithThisIdDoesNotExist() {

        this.updatingFundsDTO.setClientId(Ids.EMPTY.getId());
        assertThrowsExactly(DataMissingException.class,
                ()->this.service.putMoney(this.updatingFundsDTO));

    }

    @Test
    void testPutMoney_WhenClientWithThisIdIsExist() {

        this.updatingFundsDTO.setClientId(Ids.BANK_ACCOUNT.getId());
        assertDoesNotThrow(()->this.service
                .putMoney(this.updatingFundsDTO));
        assertEquals(0, this.bankAccount.getCurrentBalance()
                .compareTo(BigDecimal.valueOf(55)));
    }

    @Test
    void testTakeMoney_WhenClientWithThisIdDoesNotExist() {

        this.updatingFundsDTO.setClientId(Ids.EMPTY.getId());
        assertThrowsExactly(DataMissingException.class,
                ()->this.service.takeMoney(this.updatingFundsDTO));
    }

    @Test
    void testTakeMoney_WhenClientWithThisIdIsExistAndIsEnoughMoney() {

        this.updatingFundsDTO.setClientId(Ids.BANK_ACCOUNT.getId());
        assertDoesNotThrow(()->this.service
                .takeMoney(this.updatingFundsDTO));
        assertEquals(0, this.bankAccount.getCurrentBalance()
                .compareTo(BigDecimal.valueOf(45)));
    }

    @Test
    void testTakeMoney_WhenClientWithThisIdIsExistAndIsNotEnoughMoney() {

        this.bankAccount.setCurrentBalance(BigDecimal.valueOf(1.0));
        this.updatingFundsDTO.setClientId(Ids.BANK_ACCOUNT.getId());

        assertThrowsExactly(NotEnoughMoneyException.class,
                ()->this.service.takeMoney(this.updatingFundsDTO));
        assertEquals(0, this.bankAccount.getCurrentBalance()
                .compareTo(BigDecimal.valueOf(1.0)));
    }

    @Test
    void testTransferMoney_WhenSenderWithThisIdDoesNotExist () {

        this.transferMoneyDTO.setClientId(Ids.EMPTY.getId());
        this.transferMoneyDTO.setRecipientId(
                Ids.BANK_ACCOUNT_RECIPIENT.getId());

        assertThrowsExactly(DataMissingException.class,
                ()->this.service.transferMoney(
                        transferMoneyDTO));
    }

    @Test
    void testTransferMoney_WhenRecipientWithThisIdDoesNotExist () {

        this.transferMoneyDTO
                .setClientId(Ids.BANK_ACCOUNT.getId());
        this.transferMoneyDTO
                .setRecipientId(Ids.EMPTY.getId());

        assertThrowsExactly(DataMissingException.class,
                ()->this.service.transferMoney(
                        this.transferMoneyDTO));
    }

    @Test
    void testTransferMoney_WhenBothIdIsExistAndNotEnoughMoney() {

        this.bankAccount.setCurrentBalance(BigDecimal.valueOf(10));
        this.transferMoneyDTO
                .setClientId(Ids.BANK_ACCOUNT.getId());
        this.transferMoneyDTO
                .setRecipientId(Ids.BANK_ACCOUNT_RECIPIENT.getId());

        assertThrowsExactly(NotEnoughMoneyException.class,
                ()->this.service.transferMoney(
                        this.transferMoneyDTO));
    }

    @Test
    void testTransferMoney_WhenBothIdIsExistAndEnoughMoney() {

        this.transferMoneyDTO
                .setClientId(Ids.BANK_ACCOUNT.getId());
        this.transferMoneyDTO
                .setRecipientId(Ids.BANK_ACCOUNT_RECIPIENT.getId());

        assertDoesNotThrow(()->this.service.transferMoney(
                this.transferMoneyDTO));
        assertEquals(0, this.bankAccount.getCurrentBalance()
                .compareTo(BigDecimal.valueOf(35)));
        assertEquals(0, this.bankAccountRecipient.getCurrentBalance()
                .compareTo(BigDecimal.valueOf(215)));
    }
}