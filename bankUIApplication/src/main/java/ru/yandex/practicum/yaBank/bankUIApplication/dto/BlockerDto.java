package ru.yandex.practicum.yaBank.bankUIApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockerDto {
    private String currency;
    private String login;
    private String action;
    private Double amount;
}
