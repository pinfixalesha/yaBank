package ru.yandex.practicum.yaBank.cashApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.cashApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.NotificationDto;
import ru.yandex.practicum.yaBank.cashApplication.service.CashService;

@RestController
@RequestMapping("/cash")
public class CashController {

    @Autowired
    private CashService cashService;

    @PostMapping("/in")
    public HttpResponseDto cashInOperation(@RequestBody AccountOperationDto accountOperationDto) {
        cashService.cashIn(accountOperationDto);
        return HttpResponseDto.builder()
                .statusMessage("Операция выполнена успешно")
                .statusCode("0")
                .build();
    }

    @PostMapping("/out")
    public HttpResponseDto cashOutOperation(@RequestBody AccountOperationDto accountOperationDto) {
        cashService.cashOut(accountOperationDto);
        return HttpResponseDto.builder()
                .statusMessage("Операция выполнена успешно")
                .statusCode("0")
                .build();
    }

    @ExceptionHandler
    public HttpResponseDto handleException(Exception ex) {
        ex.printStackTrace();
        return HttpResponseDto.builder()
                .statusMessage(ex.getLocalizedMessage())
                .statusCode("999")
                .build();
    }

}
