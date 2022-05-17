package ru.dreremin.internetbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.internetbank.models.Client;

@Transactional
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
