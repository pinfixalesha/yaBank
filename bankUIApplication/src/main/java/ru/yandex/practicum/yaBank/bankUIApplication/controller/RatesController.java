package ru.yandex.practicum.yaBank.bankUIApplication.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.bankUIApplication.service.ExchangeApplicationService;

import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class RatesController {

    @Autowired
    private final MeterRegistry meterRegistry;

    @Value("${metricsEnabled:true}")
    private boolean metricsEnabled;

    @Autowired
    private final ExchangeApplicationService exchangeApplicationService;

    @Autowired
    private final Tracer tracer;

    @GetMapping("/api/rates")
    @Secured("ROLE_USER")
    public List<CurrencyRateDto> getRates() {
        if (metricsEnabled) {
            Timer.Sample timer = Timer.start(meterRegistry);
            try {
                // Эмуляция случайной задержки от 50 до 200 мс, чтобы ловить алерты
                long delay = 50 + new Random().nextInt(150);
                Thread.sleep(delay);

                return exchangeApplicationService.getRates();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted during sleep", e);
            } finally {
                timer.stop(meterRegistry.timer("bank_ui_get_rates_duration"));
            }
        } else {
            return exchangeApplicationService.getRates();
        }
    }
}
