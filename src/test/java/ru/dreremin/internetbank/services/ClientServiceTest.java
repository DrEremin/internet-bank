package ru.dreremin.internetbank.services;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ru.dreremin.internetbank.models.Client;
import ru.dreremin.internetbank.repositories.ClientRepository;


@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientServiceTest {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository repository;

    private Client client;

    private Instant time;

    @BeforeAll
    void beforeAll() {
        this.client = new Client(
                "Test",
                "Testov",
                "Testovich",
                "1970-01-01",
                "His address"
        );
        when(this.repository.existsById((long)0)).thenReturn(false);
        when(this.repository.existsById((long)1)).thenReturn(true);
        when(this.repository.getById((long)1)).thenReturn(this.client);
    }

    @BeforeEach
    void beforeEach() {
        this.time = Instant.now();
    }

    @AfterEach
    void afterEach() {
        log.info("run time: " + Duration.between(this.time, Instant.now()));
    }

    @Test
    void testGetClientById_IfClientWithThisIdDoesNotExist() {

        assertTrue(this.service.getClientById(0).isEmpty());
    }

    @Test
    void testGetClientById_IfClientWithThisIdIsExist() {

        assertTrue(this.service.getClientById(1).isPresent());
        assertEquals(LocalDate.of(1970, 1, 1),
                this.service.getClientById(1).get().getBirthday());
    }
}