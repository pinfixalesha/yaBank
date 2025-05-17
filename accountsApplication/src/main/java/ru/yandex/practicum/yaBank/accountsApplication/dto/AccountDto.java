package ru.yandex.practicum.yaBank.accountsApplication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    private String currency;
    private Double balance;
}
