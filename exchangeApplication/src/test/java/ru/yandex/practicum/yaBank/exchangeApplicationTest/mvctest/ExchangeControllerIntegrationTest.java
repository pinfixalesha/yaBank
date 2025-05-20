package ru.yandex.practicum.yaBank.exchangeApplicationTest.mvctest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.yaBank.exchangeApplication.ExchangeApplication;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.exchangeApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeApplication.service.CurrencyService;
import ru.yandex.practicum.yaBank.exchangeApplication.service.RatesService;
import ru.yandex.practicum.yaBank.exchangeApplicationTest.TestSecurityConfig;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {ExchangeApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ExchangeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean(reset = MockReset.BEFORE)
    private CurrencyService currencyService;

    @MockitoBean(reset = MockReset.BEFORE)
    private RatesService ratesService;


    @Test
    void testGetCurrency() throws Exception {
        List<CurrencyDto> mockCurrencies = List.of(
                new CurrencyDto("USD", "Доллар США"),
                new CurrencyDto("RUB", "Российский рубль"),
                new CurrencyDto("EUR", "Евро")
        );
        when(currencyService.getCurrency()).thenReturn(mockCurrencies);

        mockMvc.perform(get("/exchange/currency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].title").value("Доллар США"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_exchange.read")
    void testGetAllRates() throws Exception {
        List<CurrencyRateDto> mockRates = List.of(
                new CurrencyRateDto("USD", 75.0, 76.0),
                new CurrencyRateDto("EUR", 85.0, 86.0)
        );
        when(ratesService.getAllRates()).thenReturn(mockRates);

        mockMvc.perform(get("/exchange/rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].buy").value(75.0));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_exchange.write")
    void testSaveRates() throws Exception {
        List<CurrencyRateDto> mockRates = List.of(
                new CurrencyRateDto("USD", 75.0, 76.0),
                new CurrencyRateDto("EUR", 85.0, 86.0)
        );
        doNothing().when(ratesService).saveRates(mockRates);

        mockMvc.perform(post("/exchange/rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("0"))
                .andExpect(jsonPath("$.statusMessage").value("Курсы валют успешно сохранены."));
    }
}
