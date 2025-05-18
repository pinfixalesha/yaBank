package ru.yandex.practicum.yaBank.bankUIApplication.mapping;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.bankUIApplication.model.CurrencyModel;

@Component
public class CurrencyMapper {
    public CurrencyModel toModel(CurrencyDto currencyDto) {
        if (currencyDto == null) {
            return null;
        }

        return CurrencyModel.builder()
                .currencyCode(currencyDto.getCurrency()) // Маппинг поля currency -> currencyCode
                .title(currencyDto.getTitle())
                .build();
    }
}
