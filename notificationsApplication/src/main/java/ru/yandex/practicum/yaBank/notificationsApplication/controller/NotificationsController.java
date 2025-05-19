package ru.yandex.practicum.yaBank.notificationsApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;
import ru.yandex.practicum.yaBank.notificationsApplication.service.NotificationsService;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {

    @Autowired
    private NotificationsService notificationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("SCOPE_notifications.write")
    public HttpResponseDto createNotification(@RequestBody NotificationDto notification) {
        Long notificationId = notificationsService.sendNotification(notification);
        return HttpResponseDto.builder()
                .notificationId(notificationId)
                .statusMessage("Notification send OK")
                .statusCode("0")
                .build();
    }

    @ExceptionHandler
    public HttpResponseDto handleException(Exception ex) {
        ex.printStackTrace();
        return HttpResponseDto.builder()
                .notificationId(null)
                .statusMessage(ex.getLocalizedMessage())
                .statusCode("999")
                .build();
    }

}
