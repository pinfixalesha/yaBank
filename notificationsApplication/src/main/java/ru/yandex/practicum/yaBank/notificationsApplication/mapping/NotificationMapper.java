package ru.yandex.practicum.yaBank.notificationsApplication.mapping;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;
import ru.yandex.practicum.yaBank.notificationsApplication.entities.Notification;

import java.time.LocalDateTime;

@Component
public class NotificationMapper {

    public Notification toEntity(NotificationDto notificationDto) {
        if (notificationDto == null) {
            return null;
        }

        return Notification.builder()
                .email(notificationDto.getEmail())
                .applicationName(notificationDto.getApplication())
                .message(notificationDto.getMessage())
                .dateTimeCreate(LocalDateTime.now())
                .build();
    }

}
