package ru.yandex.practicum.yaBank.bankUIApplication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    private String accountNumber;
    private String currency;
    private Double balance;
}
