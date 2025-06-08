package ru.yandex.practicum.yaBank.exchangeGeneratorApplication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.dto.HttpResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RatesGenerationService {

    private static final Logger log = LoggerFactory.getLogger(RatesGenerationService.class);

    @Autowired
    private final ExchangeApplicationService exchangeApplicationService;

    @Autowired
    private final ExchangeProducer exchangeProducer;

    private static final Random random = new Random();

    @Scheduled(fixedRate = 10000)
    public void generateRates() {
        List<CurrencyRateDto> currencyRateDtos = generateRandomRates();
        log.info("Отправка курсов валют "+currencyRateDtos);
        HttpResponseDto response = exchangeProducer.sendRates(currencyRateDtos);
        //HttpResponseDto response = exchangeApplicationService.sendRates(currencyRateDtos);
        if (response!=null) {
            log.info("Результат отправки " + response.getStatusCode() + " " + response.getStatusMessage());
        } else {
            log.info("Результат отправки null");
        }
    }

    private List<CurrencyRateDto> generateRandomRates() {
        List<CurrencyRateDto> rates = new ArrayList<>();

        rates.add(createRandomRate("USD"));
        rates.add(createRandomRate("EUR"));
        rates.add(createRandomRate("RUB"));

        return rates;
    }

    private CurrencyRateDto createRandomRate(String currency) {
        if ("RUB".equals(currency)) {
            return CurrencyRateDto.builder()
                    .currency("RUB")
                    .buy(1.0)
                    .sale(1.0)
                    .build();
        }

        double buy = 70 + random.nextDouble() * 30; // Случайное значение от 70 до 100
        double sale = buy + random.nextDouble() * 5; // Продажа немного выше покупки

        BigDecimal roundedBuy = BigDecimal.valueOf(buy).setScale(2, RoundingMode.HALF_UP);
        BigDecimal roundedSale = BigDecimal.valueOf(sale).setScale(2, RoundingMode.HALF_UP);

        return CurrencyRateDto.builder()
                .currency(currency)
                .buy(roundedBuy.doubleValue())
                .sale(roundedSale.doubleValue())
                .build();
    }
}
