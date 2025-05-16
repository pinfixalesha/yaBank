package ru.yandex.practicum.yaBank.notificationsApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.yaBank.notificationsApplication.entities.Notification;

public interface NotificationsRepository extends JpaRepository<Notification, Long> {
}
