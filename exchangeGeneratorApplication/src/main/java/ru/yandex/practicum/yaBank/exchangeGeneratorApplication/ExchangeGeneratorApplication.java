package ru.yandex.practicum.yaBank.exchangeGeneratorApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableScheduling
public class ExchangeGeneratorApplication {
    private static final Logger log = LoggerFactory.getLogger(ExchangeGeneratorApplication.class);

    public static void main(String[] args) {
        System.out.println("Logger class: " + log.getClass().getName());

        SpringApplication.run(ExchangeGeneratorApplication.class, args);
    }

}
