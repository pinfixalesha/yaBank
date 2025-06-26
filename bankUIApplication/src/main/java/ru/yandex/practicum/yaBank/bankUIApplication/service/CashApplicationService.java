package ru.yandex.practicum.yaBank.bankUIApplication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountRequestDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.ChangePasswordRequestDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;

import java.util.Collections;
import java.util.List;


@Service
public class CashApplicationService {

    private static final Logger log = LoggerFactory.getLogger(CashApplicationService.class);

    @Autowired
    private RestClient restClient;

    @Autowired
    private String cashApplicationUrl;

    @Retryable(
        value = {ResourceAccessException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public HttpResponseDto cashIn(AccountOperationDto accountOperationDto) {
        log.info("Пополнение счета клиента "+accountOperationDto.getLogin());
        try {
            return restClient.post()
                    .uri(cashApplicationUrl+"/cash/in")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(accountOperationDto)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            log.error("Возникли проблемы при пополнении счета "+accountOperationDto.getLogin(),e);
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось пополнить счет клиента. Причина: " + e.getMessage())
                    .build();
        }
    }

    @Retryable(
            value = {ResourceAccessException.class}, // Повторять при ошибках соединения
            maxAttempts = 3,                        // Максимальное количество попыток
            backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public HttpResponseDto cashOut(AccountOperationDto accountOperationDto) {
        log.info("Списание со счета клиента "+accountOperationDto.getLogin());
        try {
            return restClient.post()
                    .uri(cashApplicationUrl+"/cash/out")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(accountOperationDto)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            log.error("Возникли проблемы при списании со счета "+accountOperationDto.getLogin(),e);
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось списать ДС со счета клиента. Причина: " + e.getMessage())
                    .build();
        }
    }
}
