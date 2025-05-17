package ru.yandex.practicum.yaBank.accountsApplication.configurations;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigLogger {

    @Autowired
    private Environment environment;

    @PostConstruct
    public void logProperties() {
        System.out.println("ya-bank.gateway: " + environment.getProperty("ya-bank.gateway"));
    }
}