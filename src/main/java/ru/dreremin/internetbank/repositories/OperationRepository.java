package ru.dreremin.internetbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.internetbank.models.Operation;

@Transactional(isolation = Isolation.SERIALIZABLE)
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
