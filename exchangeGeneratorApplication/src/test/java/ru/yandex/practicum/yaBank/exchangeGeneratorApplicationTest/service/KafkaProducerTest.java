package ru.yandex.practicum.yaBank.exchangeGeneratorApplicationTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.ExchangeGeneratorApplication;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.exchangeGeneratorApplicationTest.TestSecurityConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootTest(classes = {ExchangeGeneratorApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@EmbeddedKafka(topics = {"exchange"})
public class KafkaProducerTest {

    private static final Random random = new Random();

    @Autowired
    private KafkaTemplate<String, List<CurrencyRateDto>> kafkaTemplate;

    @Test
    public void testProcessor(){
        List<CurrencyRateDto> currencyRateDtos = generateRandomRates();

        String key = UUID.randomUUID().toString();
        kafkaTemplate.send("exchange", key, currencyRateDtos);

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
