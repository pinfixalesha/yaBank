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
import org.springframework.web.util.UriComponentsBuilder;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountRequestDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.ChangePasswordRequestDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserListResponseDto;

import java.util.Collections;
import java.util.List;


@Service
public class AccountApplicationService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private String accountApplicationUrl;

    @Retryable(
        value = {ResourceAccessException.class}, // Повторять при ошибках соединения
        maxAttempts = 3,                        // Максимальное количество попыток
        backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public HttpResponseDto registerUser(UserDto userDto) {
        try {
            return restClient.post()
                    .uri(accountApplicationUrl+"/user/create")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(userDto)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось создать клинета. Причина: " + e.getMessage())
                    .build();
        }
    }

    @Retryable(
            value = {ResourceAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public UserDto getUserInfo(String login) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accountApplicationUrl+"/user/findbylogin");
            builder.queryParam("login", login);

            String url = builder.toUriString();

            return restClient.get()
                    .uri(url)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(UserDto.class);
        } catch (Exception e) {
            return UserDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось получить данные клиента. Причина: " + e.getMessage())
                    .build();
        }
    }

    @Retryable(
            value = {ResourceAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public List<UserListResponseDto> getAllUsers() {
        try {
            return restClient.get()
                    .uri(accountApplicationUrl+"/user/all")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<UserListResponseDto>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Retryable(
            value = {ResourceAccessException.class}, // Повторять при ошибках соединения
            maxAttempts = 3,                        // Максимальное количество попыток
            backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public HttpResponseDto changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        try {
            return restClient.post()
                    .uri(accountApplicationUrl+"/user/changepassword")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(changePasswordRequestDto)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось создать клинета. Причина: " + e.getMessage())
                    .build();
        }
    }


    @Retryable(
            value = {ResourceAccessException.class}, // Повторять при ошибках соединения
            maxAttempts = 3,                        // Максимальное количество попыток
            backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public HttpResponseDto changeInfo(UserDto userDto) {
        try {
            return restClient.post()
                    .uri(accountApplicationUrl+"/user/edit")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(userDto)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось создать клинета. Причина: " + e.getMessage())
                    .build();
        }
    }


    @Retryable(
            value = {ResourceAccessException.class}, // Повторять при ошибках соединения
            maxAttempts = 3,                        // Максимальное количество попыток
            backoff = @Backoff(delay = 1000)        // Задержка между попытками (в миллисекундах)
    )
    public HttpResponseDto accountAdd(AccountRequestDto accountRequestDto) {
        try {
            return restClient.post()
                    .uri(accountApplicationUrl+"/account/add")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(accountRequestDto)
                    .retrieve()
                    .body(HttpResponseDto.class);
        } catch (Exception e) {
            return HttpResponseDto.builder()
                    .statusCode("500")
                    .statusMessage("Не удалось создать клинета. Причина: " + e.getMessage())
                    .build();
        }
    }

    @Retryable(
            value = {ResourceAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public List<AccountDto> getUserAccountInfo(String login) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accountApplicationUrl+"/account/all");
            builder.queryParam("login", login);

            String url = builder.toUriString();

            return restClient.get()
                    .uri(url)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<AccountDto>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
