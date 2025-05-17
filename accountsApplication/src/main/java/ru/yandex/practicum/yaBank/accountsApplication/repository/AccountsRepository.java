package ru.yandex.practicum.yaBank.accountsApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.yaBank.accountsApplication.entities.Account;
import ru.yandex.practicum.yaBank.accountsApplication.entities.User;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUser(User user);

    Optional<Account> findAccountByUserAndCurrency(User user, String currency);
}
