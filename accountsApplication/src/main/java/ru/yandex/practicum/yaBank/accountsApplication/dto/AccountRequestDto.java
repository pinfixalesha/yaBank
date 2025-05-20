package ru.yandex.practicum.yaBank.accountsApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountRequestDto {
    private String currency;
    private String login;
}
