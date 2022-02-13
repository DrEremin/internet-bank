package ru.dreremin.internetbank.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.internetbank.dto.ZonedDateTimePeriodDTO;
import ru.dreremin.internetbank.models.OperationDescription;
import ru.dreremin.internetbank.repositories.OperationDescriptionRepository;

@RequiredArgsConstructor
@Service
public class OperationDescriptionService {

    private final OperationDescriptionRepository repository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<OperationDescription> getOperationList(
            ZonedDateTimePeriodDTO dto) {

        if (dto.getIsNullableTimePoint()) {
            return repository.getAll(Sort.by(Sort.Order
                    .asc("dateTimeOfOperation")
                    .nullsLast()));
        }
        return repository.getAllWithinRange(
                dto.getStartTimePoint(),
                dto.getEndTimePoint(),
                Sort.by(Sort.Order
                        .asc("dateTimeOfOperation")
                        .nullsLast()));
    }
}
