package ru.dreremin.internetbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dreremin.internetbank.models.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
