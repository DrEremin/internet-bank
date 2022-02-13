package ru.dreremin.internetbank.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.internetbank.models.OperationDescription;

@Transactional(isolation = Isolation.SERIALIZABLE)
@Repository
public interface OperationDescriptionRepository extends
        JpaRepository<OperationDescription, Long> {

    @Query("select new OperationDescription(" +
            "o.id," +
            "o.dateTimeOfOperation, " +
            "ot.operationName, " +
            "o.amountOfOperation, " +
            "o.accountId," +
            "tr.id.recipientAccountId) " +
            "from Operation o " +
            "   join OperationType ot " +
            "       on o.operationTypeId = ot.id " +
            "   left join TransferRecipient tr " +
            "       on o.id = tr.id.operationId")
    List<OperationDescription> getAll(Sort sort);

    @Query("select new OperationDescription(" +
            "o.id," +
            "o.dateTimeOfOperation, " +
            "ot.operationName, " +
            "o.amountOfOperation, " +
            "o.accountId," +
            "tr.id.recipientAccountId) " +
            "from Operation o " +
            "   join OperationType ot " +
            "       on o.operationTypeId = ot.id " +
            "   left join TransferRecipient tr " +
            "       on o.id = tr.id.operationId " +
            "where o.dateTimeOfOperation >= :start " +
            "   and o.dateTimeOfOperation <= :end")
    List<OperationDescription> getAllWithinRange(
            @Param("start") ZonedDateTime startTimePoint,
            @Param("end")ZonedDateTime endTimePoint,
            Sort sort);
}