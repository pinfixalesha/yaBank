package ru.yandex.practicum.yaBank.blockerApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlockerApplication {
    private static final Logger log = LoggerFactory.getLogger(BlockerApplication.class);


    public static void main(String[] args) {
        System.out.println("Logger class: " + log.getClass().getName());

        SpringApplication.run(BlockerApplication.class, args);
    }

}
