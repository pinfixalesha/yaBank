package ru.yandex.practicum.yaBank.exchangeApplication.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeApplication.mapping.RatesMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeConsumerService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeConsumerService.class);

    @Autowired
    private final RatesService ratesService;

    @Autowired
    private final RatesMapper ratesMapper;

    @KafkaListener(topics = "exchange", groupId = "currency-rate-group")
    public void consumeRate(List<LinkedHashMap> currencyRate) {
        log.info("Получено сообщение из топика 'exchange', количество курсов: {}", currencyRate.size());

        List<CurrencyRateDto> currencyRateDtos=currencyRate.stream()
                .map(ratesMapper::toDto)
                .collect(Collectors.toList());

        ratesService.saveRates(currencyRateDtos);
    }
}
