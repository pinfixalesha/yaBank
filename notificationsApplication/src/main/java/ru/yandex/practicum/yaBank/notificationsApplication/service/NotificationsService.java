package ru.yandex.practicum.yaBank.notificationsApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;
import ru.yandex.practicum.yaBank.notificationsApplication.entities.Notification;
import ru.yandex.practicum.yaBank.notificationsApplication.mapping.NotificationMapper;
import ru.yandex.practicum.yaBank.notificationsApplication.repository.NotificationsRepository;

@Service
public class NotificationsService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationsRepository notificationsRepository;

    public Long sendNotification(NotificationDto notificationDto) {
        Notification notification = notificationMapper.toEntity(notificationDto);
        Notification saved = notificationsRepository.save(notification);
        System.out.print(notification);
        return saved.getId();
    }

}
