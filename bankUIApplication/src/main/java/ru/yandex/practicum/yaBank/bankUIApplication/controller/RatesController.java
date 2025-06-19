package ru.yandex.practicum.yaBank.bankUIApplication.controller;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.bankUIApplication.service.ExchangeApplicationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RatesController {

    @Autowired
    private final ExchangeApplicationService exchangeApplicationService;

    @Autowired
    private final Tracer tracer; // Brave

    @GetMapping("/api/rates")
    @Secured("ROLE_USER")
    public List<CurrencyRateDto> getRates() {
        return exchangeApplicationService.getRates();
    }
}
