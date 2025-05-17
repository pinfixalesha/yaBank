package ru.yandex.practicum.yaBank.bankUIApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.BlockerDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.ChangePasswordRequestDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;


@Service
public class BlockerApplicationService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private String blockerApplicationUrl;

    @Retryable(
        value = {ResourceAccessException.class}, // Повторять при ошибках соединения
        maxAttempts = 3,                        // Максимальное количество попыток
        backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public HttpResponseDto checkBlocker(BlockerDto blockerDto) {
        try {
            return restClient.post()
                    .uri(blockerApplicationUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(blockerDto)
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
