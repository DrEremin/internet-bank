package ru.dreremin.internetbank.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.dreremin.internetbank.dto.ZonedDateTimePeriodDTO;
import ru.dreremin.internetbank.models.OperationDescription;
import ru.dreremin.internetbank.repositories.OperationDescriptionRepository;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperationDescriptionServiceTest {

    @InjectMocks
    private OperationDescriptionService service;

    @Mock
    private OperationDescriptionRepository repository;

    private ZonedDateTimePeriodDTO dtoWithoutNull;

    private ZonedDateTimePeriodDTO dtoWithNull;

    private List<OperationDescription> descriptionsListWithinRange;

    private List<OperationDescription> descriptionsAllList;

    private Instant time;

    @BeforeAll
    void beforeAll() {

        String startDate = "2022-01-30",
                endDate = "2022-03-01",
                zoneId = "UTC+03";

        this.descriptionsListWithinRange = List.of(new OperationDescription(
                4,
                ZonedDateTime.parse("2022-02-12T21:00:00+03:00"),
                "putMoney",
                BigDecimal.valueOf(0.55),
                5,
                (long)6));

        this.descriptionsAllList = List.of(new OperationDescription(
                1,
                ZonedDateTime.parse("2022-02-02T12:00:00+03:00"),
                "getBalance",
                BigDecimal.valueOf(100.55),
                2,
                (long)3));

        this.dtoWithoutNull = new ZonedDateTimePeriodDTO(
                startDate,
                endDate,
                zoneId);

        this.dtoWithNull = new ZonedDateTimePeriodDTO(
                startDate,
                null,
                zoneId);

        when(this.repository.getAllWithinRange(
                any(ZonedDateTime.class),
                any(ZonedDateTime.class),
                any(Sort.class)))
                .thenReturn(this.descriptionsListWithinRange);

        when(this.repository.getAll(any(Sort.class)))
                .thenReturn(this.descriptionsAllList);
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
    void testGetOperationList_WhenDtoContainBothCorrectTimePoints() {
        assertEquals(this.descriptionsListWithinRange,
                this.service.getOperationList(this.dtoWithoutNull));
    }

    @Test
    void testGetOperationList_WhenDtoContainNullableTimePoint() {
        assertEquals(this.descriptionsAllList,
                this.service.getOperationList(this.dtoWithNull));
    }

}