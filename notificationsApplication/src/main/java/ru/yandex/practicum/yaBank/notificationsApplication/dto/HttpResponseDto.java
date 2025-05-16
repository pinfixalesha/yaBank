package ru.yandex.practicum.yaBank.notificationsApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HttpResponseDto {
    private String statusCode;
    private String statusMessage;
    private Long notificationId;
}
