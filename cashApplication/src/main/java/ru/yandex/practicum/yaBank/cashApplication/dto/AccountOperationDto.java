package ru.yandex.practicum.yaBank.cashApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountOperationDto {
    private String currency;
    private String login;
    private Double amount;
}
