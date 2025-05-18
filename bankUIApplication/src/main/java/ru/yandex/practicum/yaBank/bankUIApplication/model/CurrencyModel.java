package ru.yandex.practicum.yaBank.bankUIApplication.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyModel {
    private String currencyCode;
    private String title;

}
