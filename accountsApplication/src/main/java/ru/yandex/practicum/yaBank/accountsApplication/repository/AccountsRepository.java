package ru.yandex.practicum.yaBank.accountsApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.yaBank.accountsApplication.entities.Account;

import java.util.List;

public interface AccountsRepository extends JpaRepository<Account, Long> {
//    List<Account> findAllByUserId(String id);

 //   Account findByUserIdAndCurrency(String userId, String currency);
}
