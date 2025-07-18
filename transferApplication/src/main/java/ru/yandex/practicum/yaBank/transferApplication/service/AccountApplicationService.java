package ru.yandex.practicum.yaBank.transferApplication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.yaBank.transferApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.HttpResponseDto;

@Service
public class AccountApplicationService {

    private static final Logger log = LoggerFactory.getLogger(AccountApplicationService.class);

    @Autowired
    private RestClient restClient;

    @Autowired
    private String accountApplicationUrl;

    @Retryable(
            value = {ResourceAccessException.class}, // Повторять при ошибках соединения
            maxAttempts = 3,                        // Максимальное количество попыток
            backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public HttpResponseDto cashIn(AccountOperationDto accountOperationDto) {
        log.info("Пополнение счета "+accountOperationDto.getCurrency()+" клиенту "+accountOperationDto.getLogin());
        try {
            return restClient.put()
                    .uri(accountApplicationUrl+"/account/cashIn")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(accountOperationDto)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            log.error("Проблемы пополнение счета "+accountOperationDto.getCurrency()+" клиенту "+accountOperationDto.getLogin(),e);
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось выполнить операцию. Причина: " + e.getMessage())
                    .build();
        }
    }

    @Retryable(
            value = {ResourceAccessException.class}, // Повторять при ошибках соединения
            maxAttempts = 3,                        // Максимальное количество попыток
            backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public HttpResponseDto cashOut(AccountOperationDto accountOperationDto) {
        log.info("Списание со счета "+accountOperationDto.getCurrency()+" клиенту "+accountOperationDto.getLogin());
        try {
            return restClient.put()
                    .uri(accountApplicationUrl+"/account/cashOut")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(accountOperationDto)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            log.error("Проблемы списание со счета "+accountOperationDto.getCurrency()+" клиенту "+accountOperationDto.getLogin(),e);
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось выполнить операцию. Причина: " + e.getMessage())
                    .build();
        }
    }
}
