package ru.yandex.practicum.yaBank.bankUIApplication.dto;

import lombok.Data;

@Data
public class AccountRequestDto {
    private String currency;
    private String login;
}
