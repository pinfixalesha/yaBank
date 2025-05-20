package ru.yandex.practicum.yaBank.accountsApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChangePasswordRequestDto {
    private String login;
    private String password;
}
