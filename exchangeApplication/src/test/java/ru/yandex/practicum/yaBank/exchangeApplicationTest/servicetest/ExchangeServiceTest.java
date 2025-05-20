package ru.yandex.practicum.yaBank.exchangeApplicationTest.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.yaBank.exchangeApplication.ExchangeApplication;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.exchangeApplication.repository.RatesRepository;
import ru.yandex.practicum.yaBank.exchangeApplication.service.CurrencyService;
import ru.yandex.practicum.yaBank.exchangeApplication.service.RatesService;
import ru.yandex.practicum.yaBank.exchangeApplicationTest.TestSecurityConfig;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ExchangeApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ExchangeServiceTest {

    @Autowired
    private RatesRepository ratesRepository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private RatesService ratesService;


    @BeforeEach
    void setUp() {
        ratesRepository.deleteAll();
    }

    @Test
    void testGetCurrency() {
        // Act
        List<CurrencyDto> result = currencyService.getCurrency();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testSaveRates() {
        // Arrange
        List<CurrencyRateDto> mockRates = List.of(
                new CurrencyRateDto("USD", 75.0, 76.0),
                new CurrencyRateDto("EUR", 85.0, 86.0)
        );
        ratesService.saveRates(mockRates);

        List<CurrencyRateDto> currencyRateDtos=ratesService.getAllRates();

        // Assert
        assertNotNull(currencyRateDtos);
        assertEquals(2, currencyRateDtos.size());
    }

}
