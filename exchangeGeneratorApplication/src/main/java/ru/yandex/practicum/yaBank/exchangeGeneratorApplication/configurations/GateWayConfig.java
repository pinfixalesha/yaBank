package ru.yandex.practicum.yaBank.exchangeGeneratorApplication.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GateWayConfig {

    @Value("${ya-bank.gateway}")
    private String gatewayUrl;

    static private String EXCHANGE_APPLICATION = "exchangeApplication";

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public String exchangeApplicationUrl() {
        return gatewayUrl + "/" + EXCHANGE_APPLICATION +"/exchange/rates";
    }


}
