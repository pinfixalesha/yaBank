package ru.yandex.practicum.yaBank.notificationsApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationsApplication {

    private static final Logger log = LoggerFactory.getLogger(NotificationsApplication.class);

    public static void main(String[] args) {
        System.out.println("Logger class: " + log.getClass().getName());

        SpringApplication.run(NotificationsApplication.class, args);
    }

}
