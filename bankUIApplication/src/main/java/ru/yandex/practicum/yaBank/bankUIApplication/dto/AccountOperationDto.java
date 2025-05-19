package ru.yandex.practicum.yaBank.bankUIApplication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountOperationDto {
    private String currency;
    private String login;
    private Double amount;
}
