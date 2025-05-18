package ru.yandex.practicum.yaBank.exchangeApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeApplication.entities.Rate;
import ru.yandex.practicum.yaBank.exchangeApplication.mapping.RatesMapper;
import ru.yandex.practicum.yaBank.exchangeApplication.repository.RatesRepository;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatesService {

    @Autowired
    private RatesMapper ratesMapper;

    @Autowired
    private RatesRepository ratesRepository;

    @Transactional
    public void saveRates(List<CurrencyRateDto> currencyRateDtos) {
        List<Rate> rates = currencyRateDtos.stream()
                .map(ratesMapper::toEntity)
                .collect(Collectors.toList());
        ratesRepository.deleteAll();
        ratesRepository.saveAll(rates);
    }

    public List<CurrencyRateDto> getAllRates() {
        List<Rate> rates = ratesRepository.findAll();

        return rates.stream()
                .map(ratesMapper::toDto)
                .collect(Collectors.toList());
    }
}
