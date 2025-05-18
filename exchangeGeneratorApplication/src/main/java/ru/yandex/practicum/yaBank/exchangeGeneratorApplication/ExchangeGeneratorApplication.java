package ru.yandex.practicum.yaBank.exchangeGeneratorApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExchangeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeGeneratorApplication.class, args);
    }

}
