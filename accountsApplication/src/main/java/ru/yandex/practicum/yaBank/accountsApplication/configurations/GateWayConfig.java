package ru.yandex.practicum.yaBank.accountsApplication.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GateWayConfig {

    @Value("${ya-bank.gateway}")
    private String gatewayUrl;

    static private String NOTIFICATIONS_APPLICATION = "notificationsapplication";

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public String notificationsUrl() {
        return gatewayUrl + "/" + NOTIFICATIONS_APPLICATION + "/notifications";
    }
}
