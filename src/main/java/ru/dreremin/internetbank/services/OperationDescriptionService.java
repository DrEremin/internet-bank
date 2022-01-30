package ru.dreremin.internetbank.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.dreremin.internetbank.dto.DateTimesOfPeriodWithZoneIdDTO;
import ru.dreremin.internetbank.models.OperationDescription;
import ru.dreremin.internetbank.repositories.OperationDescriptionRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OperationDescriptionService {

    private final OperationDescriptionRepository repository;

    public OperationDescriptionService(
            OperationDescriptionRepository repository) {

        this.repository = repository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<OperationDescription> getOperationList(
            DateTimesOfPeriodWithZoneIdDTO dto) {

        Optional<ZonedDateTime> optionalStartTimePoint =
                Optional.ofNullable(dto.getStartZonedDateTime());

        Optional<ZonedDateTime> optionalEndTimePoint =
                Optional.ofNullable(dto.getEndZonedDateTime());

        if (optionalStartTimePoint.isPresent()
                && optionalEndTimePoint.isPresent()) {
            return repository.getAllWithinRange(
                    dto.getStartZonedDateTime(),
                    dto.getEndZonedDateTime(),
                    Sort.by(Sort.Order
                            .asc("timeAndDateOfOperation")
                            .nullsLast()));
        }
        return repository.getAll(Sort.by(Sort.Order
                        .asc("timeAndDateOfOperation")
                        .nullsLast()));
    }
}
