package ru.dreremin.internetbank.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.dreremin.internetbank.models.BankAccount;

@Transactional(isolation = Isolation.SERIALIZABLE)
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> getBankAccountByClientId(Long clientId);
}
