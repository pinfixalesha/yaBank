package ru.yandex.practicum.yaBank.exchangeApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.exchangeApplication.service.CurrencyService;
import ru.yandex.practicum.yaBank.exchangeApplication.service.RatesService;

import java.util.List;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    @Autowired
    private RatesService ratesService;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("currency")
    public List<CurrencyDto> getCurrency() {
        return currencyService.getCurrency();
    }

    @PostMapping("rates")
    @Secured("SCOPE_exchange.write")
    public HttpResponseDto saveRates(@RequestBody List<CurrencyRateDto> currencyRateDtos) {
        ratesService.saveRates(currencyRateDtos);
        return HttpResponseDto.builder()
                .statusMessage("Курсы валют успешно сохранены.")
                .statusCode("0")
                .build();
    }

    @GetMapping("rates")
    @Secured("SCOPE_exchange.read")
    public List<CurrencyRateDto> getAllRates() {
        return ratesService.getAllRates();
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
