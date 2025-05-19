package ru.yandex.practicum.yaBank.accountsApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.service.AccountsService;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountsService accountsService;

    @ExceptionHandler
    public HttpResponseDto handleException(Exception ex) {
        ex.printStackTrace();
        return HttpResponseDto.builder()
                .statusMessage(ex.getLocalizedMessage())
                .statusCode("999")
                .build();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("SCOPE_accounts.write")
    public HttpResponseDto createAccount(@RequestBody AccountRequestDto accountRequestDto) {
        Long accountId = accountsService.addAccount(accountRequestDto);
        return HttpResponseDto.builder()
                .statusMessage("Create Account User OK")
                .statusCode("0")
                .build();
    }

    @PutMapping("/cashIn")
    @Secured("SCOPE_accounts.write")
    public HttpResponseDto cashInOperation(@RequestBody AccountOperationDto accountOperationDto) {
        accountsService.cashIn(accountOperationDto);
        return HttpResponseDto.builder()
                .statusMessage("Операция выполнена успешно")
                .statusCode("0")
                .build();
    }

    @PutMapping("/cashOut")
    @Secured("SCOPE_accounts.write")
    public HttpResponseDto cashOutOperation(@RequestBody AccountOperationDto accountOperationDto) {
        accountsService.cashOut(accountOperationDto);
        return HttpResponseDto.builder()
                .statusMessage("Операция выполнена успешно")
                .statusCode("0")
                .build();
    }

    @GetMapping("/all")
    @Secured("SCOPE_accounts.read")
    public List<AccountDto> getAccountsByUsername(@RequestParam("login") String login) {
        return accountsService.findAccountsByLogin(login);
    }

    @GetMapping("/get")
    @Secured("SCOPE_accounts.read")
    public AccountDto getAccountInfo(@RequestParam("currency") String currency, @RequestParam("login") String login) {
        return accountsService.findAccountByLoginAndCurrency(login, currency);
    }

}
