package ru.yandex.practicum.yaBank.notificationsApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private String email;
    private String message;
    private String application;

}
