package ru.yandex.practicum.yaBank.bankUIApplication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserListResponseDto {
    private String login;
    private String fio;
    private String email;
    private String dateOfBirth;
}
