package ru.yandex.practicum.yaBank.cashApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CashApplication {
    private static final Logger log = LoggerFactory.getLogger(CashApplication.class);

    public static void main(String[] args) {
        System.out.println("Logger class: " + log.getClass().getName());
        SpringApplication.run(CashApplication.class, args);
    }

}
