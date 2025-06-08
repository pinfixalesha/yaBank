package ru.yandex.practicum.yaBank.notificationsApplication.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;

@Service
@RequiredArgsConstructor
public class NotificationsConsumerService {

    private static final Logger log = LoggerFactory.getLogger(NotificationsConsumerService.class);

    @Autowired
    private final NotificationsService notificationsService;

    @KafkaListener(topics = "notifications", groupId = "currency-rate-group")
    public void consumeRate(NotificationDto notificationDto) {
        log.info("Получено сообщение из топика 'notifications'");
        notificationsService.sendNotification(notificationDto);
    }
}
