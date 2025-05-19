package ru.yandex.practicum.yaBank.cashApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.cashApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.cashApplication.service.CashService;

@RestController
@RequestMapping("/cash")
public class CashController {

    @Autowired
    private CashService cashService;

    @PostMapping("/in")
    @Secured("SCOPE_cash.write")
    public HttpResponseDto cashInOperation(@RequestBody AccountOperationDto accountOperationDto) {
        cashService.cashIn(accountOperationDto);
        return HttpResponseDto.builder()
                .statusMessage("Операция выполнена успешно")
                .statusCode("0")
                .build();
    }

    @PostMapping("/out")
    @Secured("SCOPE_cash.write")
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
