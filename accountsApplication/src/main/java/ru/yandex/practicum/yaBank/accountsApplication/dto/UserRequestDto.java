package ru.yandex.practicum.yaBank.accountsApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestDto {
    private String login;
    private String password;
    private String fio;
    private String role;
    private String email;
    private String dateOfBirth;
}
