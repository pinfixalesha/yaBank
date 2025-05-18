package ru.yandex.practicum.yaBank.exchangeGeneratorApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.dto.HttpResponseDto;

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
    public HttpResponseDto sendRates(List<CurrencyRateDto> currencyRateDtos) {
        try {
            return restClient.post()
                    .uri(exchangeApplicationUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(currencyRateDtos)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось проверить подозрительную операцию. Причина: " + e.getMessage())
                    .build();
        }
    }

}
