package ru.yandex.practicum.yaBank.bankUIApplication.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.BlockerDto;

@Configuration
public class GateWayConfig {

    @Value("${ya-bank.gateway}")
    private String gatewayUrl;

    static private String ACCOUNT_APPLICATION = "accountsapplication";

    static private String BLOCKER_APPLICATION = "blockerapplication";

    @Bean
    public RestClient restClient() {
        return RestClient.create();
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
