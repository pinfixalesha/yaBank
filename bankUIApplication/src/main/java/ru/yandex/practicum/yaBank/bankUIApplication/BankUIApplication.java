package ru.yandex.practicum.yaBank.bankUIApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankUIApplication {
    private static final Logger log = LoggerFactory.getLogger(BankUIApplication.class);


    public static void main(String[] args) {
        System.out.println("Logger class: " + log.getClass().getName());

        SpringApplication.run(BankUIApplication.class, args);
    }

}
