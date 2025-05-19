package ru.yandex.practicum.yaBank.cashApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockerDto {
    private String currency;
    private String login;
    private String action;
    private Double amount;
}
