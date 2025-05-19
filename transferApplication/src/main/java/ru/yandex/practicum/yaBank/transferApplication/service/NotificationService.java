package ru.yandex.practicum.yaBank.transferApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.yaBank.transferApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.NotificationDto;


@Service
public class NotificationService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private String notificationsUrl;

    @Retryable(
        value = {ResourceAccessException.class}, // Повторять при ошибках соединения
        maxAttempts = 3,                        // Максимальное количество попыток
        backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public HttpResponseDto sendNotification(String email, String message) {

        NotificationDto notificationDto = NotificationDto.builder()
                .application("Cash")
                .email(email)
                .message(message)
                .build();

        try {
            return restClient.post()
                    .uri(notificationsUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(notificationDto)
                    .retrieve()
                    .body(HttpResponseDto.class); // Десериализуем ответ в HttpResponseDto
        } catch (Exception e) {
            // Если сервис недоступен, выводим сообщение об ошибке
            System.err.println("Не удалось отправить уведомление. Причина: " + e.getMessage());
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось отправить уведомление. Причина: " + e.getMessage())
                    .build();
        }
    }

}
