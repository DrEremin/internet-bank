package ru.dreremin.internetbank.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.dreremin.internetbank.models.BankAccount;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    Optional<BankAccount> getBankAccountByUserId(Long userId);
}
