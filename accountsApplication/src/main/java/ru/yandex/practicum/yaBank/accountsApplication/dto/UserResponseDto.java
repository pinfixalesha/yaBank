package ru.yandex.practicum.yaBank.accountsApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private String login;
    private String password;
    private String fio;
    private String role;
    private String email;
    private String dateOfBirth;
    private String statusCode;
    private String statusMessage;
}
