package ru.yandex.practicum.yaBank.bankUIApplication.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AccountsApplicationConfig {

    static private String ACCOUNT_PATH="http://gateway/accountsApplication";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
