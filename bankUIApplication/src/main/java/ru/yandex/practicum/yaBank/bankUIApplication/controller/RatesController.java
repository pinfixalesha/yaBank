package ru.yandex.practicum.yaBank.bankUIApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.bankUIApplication.service.ExchangeApplicationService;

import java.util.List;

@RestController
public class RatesController {

    @Autowired
    private ExchangeApplicationService exchangeApplicationService;

    @GetMapping("/api/rates")
    @Secured("ROLE_USER")
    public List<CurrencyRateDto> getRates() {
        return exchangeApplicationService.getRates();
    }
}
