package ru.yandex.practicum.yaBank.accountsApplication.dto;

import lombok.Data;

@Data
public class AccountRequestDto {
    private String currency;
    private String login;
}
