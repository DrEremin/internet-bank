package ru.dreremin.internetbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dreremin.internetbank.models.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
