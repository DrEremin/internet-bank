package ru.dreremin.internetbank.controllers;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request
        .MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request
        .MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result
        .MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result
        .MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet
        .AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ru.dreremin.internetbank.dto.impl.ClientIdDTO;
import ru.dreremin.internetbank.exceptions.DataMissingException;
import ru.dreremin.internetbank.exceptions.IncorrectNumberException;
import ru.dreremin.internetbank.exceptions.UniquenessViolationException;
import ru.dreremin.internetbank.models.BankAccount;
import ru.dreremin.internetbank.repositories.BankAccountRepository;
import ru.dreremin.internetbank.services.BankAccountManager;
import ru.dreremin.internetbank.services.ClientService;
import ru.dreremin.internetbank.util.JsonConverter;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManagementBankAccountControllerTest {

    @Autowired
    private BankAccountManager manager;

    @MockBean
    private ClientService service;

    @MockBean
    private BankAccountRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Instant time;

    private ClientIdDTO dto;

    private long incrementId;

    private String json;

    @BeforeAll
    void beforeAll() {

        /*this.manager = new BankAccountManager(
                this.service,
                this.repository);*/

        this.dto = new ClientIdDTO(
                1.0,
                "1970-01-01",
                "01:23:45",
                "UTC+03");

        this.incrementId = 1;
    }

    @BeforeEach
    void beforeEach() throws Exception {

        this.dto.setClientId(this.incrementId++);
        this.json = JsonConverter.serializeToJson(this.dto);
        this.time = Instant.now();
    }

    @AfterEach
    void afterEach() {
        log.info("run time: " + Duration.between(time, Instant.now()));
    }

    @Test
    void testCreateAccount_IfRequestIsCorrectAndBankAccountDoesNotExist()
            throws Exception {

        when(this.service.isClientWithThisIdExist(this.dto.getClientId()))
                .thenReturn(true);
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(put("/account-management/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(1)))
                .andExpect(jsonPath("$.message", is("Ok")));
    }

    @Test
    void testCreateAccount_IfClientIdInBodyOfRequestIsRealNumber()
            throws Exception {

        String clientId = String.valueOf(this.dto.getClientId());
        String newClientId = String.valueOf(Double.valueOf(clientId + ".2"));

        this.json = this.json.replaceFirst(clientId, newClientId);

        when(this.service.isClientWithThisIdExist(this.dto.getClientId()))
                .thenReturn(true);
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(put("/account-management/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(-1)))
                .andExpect(jsonPath("$.message",
                        is("Value of user id must not be real number")))
                .andExpect(r->assertInstanceOf(IncorrectNumberException.class,
                        r.getResolvedException()));
    }

    @Test
    void testCreateAccount_IfDateFormatInBodyOfRequestIsIncorrect()
            throws Exception {

        this.json = this.json.replaceFirst("1970-01-01", "1970-01:01");

        when(this.service.isClientWithThisIdExist(this.dto.getClientId()))
                .thenReturn(true);
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(put("/account-management/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(-1)))
                .andExpect(jsonPath("$.message",
                        is("Incorrect date or time format")));
    }

    @Test
    void testCreateAccount_IfTimeZoneFormatInBodyOfRequestIsIncorrect()
            throws Exception {

        this.json = this.json.replaceFirst("UTC\\+03:00", "123123");

        when(this.service.isClientWithThisIdExist(this.dto.getClientId()))
                .thenReturn(true);
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(put("/account-management/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(-1)))
                .andExpect(jsonPath("$.message",
                        is("Zone id has an invalid format")));
    }

    @Test
    void testCreateAccount_IfTimeZoneInBodyOfRequestIsNotFound()
            throws Exception {

        this.json = this.json.replaceFirst("UTC\\+03:00", "Russia/Moscow");

        when(this.service.isClientWithThisIdExist(this.dto.getClientId()))
                .thenReturn(true);
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(put("/account-management/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(0)))
                .andExpect(jsonPath("$.message",
                        is("Zone id is a region ID that cannot be found")));
    }

    @Test
    void testCreateAccount_IfRequestIsCorrectButClientWithThisIdIsNotExist()
            throws Exception {

        when(this.service.isClientWithThisIdExist(this.dto.getClientId()))
                .thenReturn(false);
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(put("/account-management/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(0)))
                .andExpect(jsonPath("$.message",
                        is("Client with this ID not found")))
                .andExpect(r->assertInstanceOf(DataMissingException.class,
                        r.getResolvedException()));
    }

    @Test
    void testCreateAccount_IfRequestIsCorrectButBankAccountWithThisIdIsExist()
            throws Exception {

        BankAccount bankAccount = new BankAccount(this.dto.getClientId());
        bankAccount.setId(0);

        when(this.service.isClientWithThisIdExist(this.dto.getClientId()))
                .thenReturn(true);
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.of(bankAccount));

        this.mockMvc.perform(put("/account-management/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(0)))
                .andExpect(jsonPath("$.message",
                        is("Client account with this id already exists")))
                .andExpect(r->assertInstanceOf(
                        UniquenessViolationException.class,
                        r.getResolvedException()));
    }

    @Test
    void testDeleteAccount_IfRequestIsCorrectAndBankAccountIsExist()
            throws Exception {

        BankAccount bankAccount = new BankAccount(this.dto.getClientId());
        bankAccount.setId(0);

        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.of(bankAccount));

        this.mockMvc.perform(delete("/account-management/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(1)))
                .andExpect(jsonPath("$.message", is("Ok")));
    }

    @Test
    void testDeleteAccount_IfClientIdInBodyOfRequestIsRealNumber()
            throws Exception {

        BankAccount bankAccount = new BankAccount(this.dto.getClientId());
        bankAccount.setId(0);

        String clientId = String.valueOf(this.dto.getClientId());
        String newClientId = String.valueOf(Double.valueOf(clientId + ".2"));

        this.json = this.json.replaceFirst(clientId, newClientId);
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.of(bankAccount));

        this.mockMvc.perform(delete("/account-management/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(-1)))
                .andExpect(jsonPath("$.message",
                        is("Value of user id must not be real number")))
                .andExpect(r->assertInstanceOf(IncorrectNumberException.class,
                        r.getResolvedException()));
    }

    @Test
    void testDeleteAccount_IfDateFormatInBodyOfRequestIsIncorrect()
            throws Exception {

        this.json = this.json.replaceFirst("1970-01-01", "1970-01:01");
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(delete("/account-management/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(-1)))
                .andExpect(jsonPath("$.message",
                        is("Incorrect date or time format")));
    }

    @Test
    void testDeleteAccount_IfTimeZoneFormatInBodyOfRequestIsIncorrect()
            throws Exception {

        this.json = this.json.replaceFirst("UTC\\+03:00", "123123");
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(delete("/account-management/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(-1)))
                .andExpect(jsonPath("$.message",
                        is("Zone id has an invalid format")));
    }

    @Test
    void testDeleteAccount_IfTimeZoneInBodyOfRequestIsNotFound()
            throws Exception {

        this.json = this.json.replaceFirst("UTC\\+03:00", "Russia/Moscow");
        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(delete("/account-management/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(0)))
                .andExpect(jsonPath("$.message",
                        is("Zone id is a region ID that cannot be found")));
    }

    @Test
    void testDeleteAccount_IfRequestIsCorrectButBankAccountIsNotExist()
            throws Exception {

        when(this.repository.getBankAccountByClientId(this.dto.getClientId()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(delete("/account-management/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.json))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",
                        is(0)))
                .andExpect(jsonPath("$.message",
                        is("Client with this ID or his bank account " +
                                "does not exist")))
                .andExpect(r->assertInstanceOf(
                        DataMissingException.class,
                        r.getResolvedException()));
    }
}