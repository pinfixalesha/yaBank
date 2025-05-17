package ru.yandex.practicum.yaBank.bankUIApplication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequestDto {
    private String login;
    private String password;
}
