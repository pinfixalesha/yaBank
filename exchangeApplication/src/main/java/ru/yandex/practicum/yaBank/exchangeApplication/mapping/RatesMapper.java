package ru.yandex.practicum.yaBank.exchangeApplication.mapping;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeApplication.entities.Rate;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

@Component
public class RatesMapper {

    public Rate toEntity(CurrencyRateDto currencyRateDto) {
        if (currencyRateDto == null) {
            return null;
        }

        return Rate.builder()
                .currency(currencyRateDto.getCurrency())
                .buy(BigDecimal.valueOf(currencyRateDto.getBuy()))
                .sale(BigDecimal.valueOf(currencyRateDto.getSale()))
                .build();
    }

    public CurrencyRateDto toDto(Rate rate) {
        if (rate == null) {
            return null;
        }

        return CurrencyRateDto.builder()
                .currency(rate.getCurrency())
                .buy(rate.getBuy().doubleValue())
                .sale(rate.getSale().doubleValue())
                .build();
    }

    public CurrencyRateDto toDto(LinkedHashMap rate) {
        if (rate == null) {
            return null;
        }

        return CurrencyRateDto.builder()
                .currency((String) rate.get("currency"))
                .buy(((Double) rate.get("buy")).doubleValue())
                .sale(((Double) rate.get("sale")).doubleValue())
                .build();
    }

}
