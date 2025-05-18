package ru.yandex.practicum.yaBank.bankUIApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyRateDto;

import java.util.Collections;
import java.util.List;


@Service
public class ExchangeApplicationService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private String exchangeApplicationUrl;

    @Retryable(
        value = {ResourceAccessException.class}, // Повторять при ошибках соединения
        maxAttempts = 3,                        // Максимальное количество попыток
        backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public List<CurrencyRateDto> getRates() {
        try {
            return restClient.get()
                    .uri(exchangeApplicationUrl+"/rates")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CurrencyRateDto>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Retryable(
            value = {ResourceAccessException.class}, // Повторять при ошибках соединения
            maxAttempts = 3,                        // Максимальное количество попыток
            backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public List<CurrencyDto> getCurrency() {
        try {
            return restClient.get()
                    .uri(exchangeApplicationUrl+"/currency")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CurrencyDto>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
