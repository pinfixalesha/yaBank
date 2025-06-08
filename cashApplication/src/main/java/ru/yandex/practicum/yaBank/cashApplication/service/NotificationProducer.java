package ru.yandex.practicum.yaBank.cashApplication.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.HttpResponseDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private static final Logger log = LoggerFactory.getLogger(NotificationProducer.class);

    @Autowired
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    public HttpResponseDto sendNotification(String email, String message) {

        NotificationDto notificationDto = NotificationDto.builder()
                .application("Accounts")
                .email(email)
                .message(message)
                .build();

        log.info("Отправка данных уведомления");
        try {
            String key = UUID.randomUUID().toString();
            kafkaTemplate.send("notifications",key, notificationDto).get();
            return HttpResponseDto.builder()
                    .statusCode("200")
                    .statusMessage("OK")
                    .build();
        } catch (Exception e) {
            log.error("Возникла проблема при отправке сообщения",e);
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось отправить сообщение. Причина: " + e.getMessage())
                    .build();
        }
    }
}
