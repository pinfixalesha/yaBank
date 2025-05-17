package ru.yandex.practicum.yaBank.accountsApplication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String login;
    private String password;
    private String fio;
    private String role;
    private String dateOfBirth;
}
