package ru.yandex.practicum.yaBank.exchangeGeneratorApplicationTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.ExchangeGeneratorApplication;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.service.ExchangeApplicationService;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.service.ExchangeProducer;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.service.RatesGenerationService;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplicationTest.TestSecurityConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ExchangeGeneratorApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
public class RatesGenerationServiceUnitTest {

    @Autowired
    private RatesGenerationService ratesGenerationService;

    @MockitoBean(reset = MockReset.BEFORE)
    private ExchangeApplicationService exchangeApplicationService;

    @MockitoBean(reset = MockReset.BEFORE)
    private ExchangeProducer exchangeProducer;

    @Test
    void testGenerateRandomRates() {
        ratesGenerationService.generateRates();

        HttpResponseDto mockResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("Rates sent successfully")
                .build();

        when(exchangeApplicationService.sendRates(anyList())).thenReturn(mockResponse);
        when(exchangeProducer.sendRates(anyList())).thenReturn(mockResponse);

        verify(exchangeProducer, times(1)).sendRates(anyList());
    }
}
