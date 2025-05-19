package ru.yandex.practicum.yaBank.cashApplication.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GateWayConfig {

    @Value("${ya-bank.gateway}")
    private String gatewayUrl;

    static private String ACCOUNT_APPLICATION = "accountsapplication";

    static private String BLOCKER_APPLICATION = "blockerapplication";

    static private String NOTIFICATIONS_APPLICATION = "notificationsapplication";

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public String notificationsUrl() {
        return gatewayUrl + "/" + NOTIFICATIONS_APPLICATION + "/notifications";
    }

    @Bean
    public String accountApplicationUrl() {
        return gatewayUrl + "/" + ACCOUNT_APPLICATION;
    }

    @Bean
    public String blockerApplicationUrl() {
        return gatewayUrl + "/" + BLOCKER_APPLICATION + "/blocker";
    }

}
