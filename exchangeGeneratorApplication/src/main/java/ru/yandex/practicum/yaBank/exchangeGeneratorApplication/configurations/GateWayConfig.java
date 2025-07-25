package ru.yandex.practicum.yaBank.exchangeGeneratorApplication.configurations;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class GateWayConfig {

    @Autowired
    private final Tracer tracer;

    @Value("${ya-bank.gateway}")
    private String gatewayUrl;

    @Value("${spring.security.oauth2.client.registration.service-client.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.service-client.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
    private String tokenUri;

    static private String EXCHANGE_APPLICATION = "exchangeApplication";

    @Bean
    public String exchangeApplicationUrl() {
        return gatewayUrl + "/" + EXCHANGE_APPLICATION +"/exchange/rates";
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration keycloakClient = ClientRegistration.withRegistrationId("oauth-yabank")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri(tokenUri)
                .scope("exchange.write")
                .build();
        return new InMemoryClientRegistrationRepository(keycloakClient);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        manager.setAuthorizedClientProvider(OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials() // Для межсервисной аутентификации
                .refreshToken()      // Для обновления токенов
                .build());

        return manager;
    }

    public final String buildTraceParent(String traceId,String spanId) {
        return String.format("00-%s-%s-01", traceId, spanId);
    }

    @Bean
    public RestClient restClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        OAuth2ClientHttpRequestInterceptor requestInterceptor =
                new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
        requestInterceptor.setClientRegistrationIdResolver(request -> "oauth-yabank");
        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    Span currentSpan = tracer.currentSpan();
                    if (currentSpan != null) {
                        String traceParent = buildTraceParent(
                                currentSpan.context().traceId(),
                                currentSpan.context().spanId()
                        );
                        request.getHeaders().set("traceparent", traceParent);
                    }

                    return requestInterceptor.intercept(request, body, execution);
                })
                .build();
    }
}
