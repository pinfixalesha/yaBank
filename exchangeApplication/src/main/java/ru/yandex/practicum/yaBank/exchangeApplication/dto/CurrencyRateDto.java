package ru.yandex.practicum.yaBank.exchangeApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyRateDto {
    private String currency;
    private Double buy;
    private Double sale;

}
