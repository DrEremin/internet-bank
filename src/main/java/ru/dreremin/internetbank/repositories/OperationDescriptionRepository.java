package ru.dreremin.internetbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.dreremin.internetbank.models.OperationDescription;

import java.util.List;

@SuppressWarnings("ALL")
@Transactional(isolation = Isolation.SERIALIZABLE)
@Repository
public interface OperationDescriptionRepository extends
        JpaRepository<OperationDescription, Long> {

    @Query(value = "select operation.id" +
            ", date_time" +
            ", operation_name" +
            ", transaction_amount" +
            ", operation.account_id" +
            ", recipient_account_id " +
            "from operation " +
            "left join transfer_recipient " +
            "on operation.id = transfer_recipient.operation_id " +
            "left join operation_type " +
            "on operation_type_id = operation_type.id", nativeQuery = true)
    List<OperationDescription> getAll();
}
