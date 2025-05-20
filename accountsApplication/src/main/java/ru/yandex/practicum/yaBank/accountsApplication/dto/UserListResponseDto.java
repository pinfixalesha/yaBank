package ru.yandex.practicum.yaBank.accountsApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserListResponseDto {
    private String login;
    private String fio;
    private String email;
    private String dateOfBirth;
}
