package ru.yandex.practicum.yaBank.accountsApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.yaBank.accountsApplication.entities.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);
}
