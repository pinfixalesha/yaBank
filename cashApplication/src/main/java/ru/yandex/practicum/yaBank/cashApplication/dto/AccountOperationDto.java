package ru.yandex.practicum.yaBank.cashApplication.dto;

import lombok.Data;

@Data
public class AccountOperationDto {
    private String currency;
    private String login;
    private Double amount;
}
