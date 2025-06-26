package ru.yandex.practicum.yaBank.bankUIApplication.service;

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
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.TransferOperationDto;


@Service
public class TransferApplicationService {

    private static final Logger log = LoggerFactory.getLogger(TransferApplicationService.class);

    @Autowired
    private RestClient restClient;

    @Autowired
    private String transferApplicationUrl;

    @Retryable(
        value = {ResourceAccessException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public HttpResponseDto transfer(TransferOperationDto transferOperationDto) {
        log.info("Выполнение денежного перевода с "+transferOperationDto.getFromLogin()+" к "+transferOperationDto.getToLogin());
        try {
            return restClient.post()
                    .uri(transferApplicationUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(transferOperationDto)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            log.error("Возникли проблемы денежного перевода с "+transferOperationDto.getFromLogin()+" к "+transferOperationDto.getToLogin(),e);
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось выполнить операцию. Причина: " + e.getMessage())
                    .build();
        }
    }


}
