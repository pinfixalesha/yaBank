package ru.yandex.practicum.yaBank.bankUIApplication.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
public class GateWayConfig {

    @Value("${ya-bank.gateway}")
    private String gatewayUrl;

    static private String ACCOUNT_APPLICATION = "accountsapplication";

    static private String EXCHANGE_APPLICATION = "exchangeApplication";

    static private String CASH_APPLICATION = "cashApplication";

    static private String TRANSFER_APPLICATION = "transferApplication";

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public String accountApplicationUrl() {
        return gatewayUrl + "/" + ACCOUNT_APPLICATION;
    }

    @Bean
    public String exchangeApplicationUrl() {
        return gatewayUrl + "/" + EXCHANGE_APPLICATION +"/exchange";
    }

    @Bean
    public String cashApplicationUrl() {
        return gatewayUrl + "/" + CASH_APPLICATION;
    }

    @Bean
    public String transferApplicationUrl() {
        return gatewayUrl + "/" + TRANSFER_APPLICATION+"/transfer";
    }

}
