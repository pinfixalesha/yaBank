package ru.yandex.practicum.yaBank.cashApplicationTest;

import com.netflix.discovery.EurekaClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        return token -> {
            // Возвращаем фиктивный JWT с нужными claims
            return Jwt.withTokenValue("test-token")
                    .header("alg", "none")
                    .claim("scope", "notifications.write") // Пример scope
                    .build();
        };
    }

    @Bean
    public EurekaClient eurekaClient() {
        return mock(EurekaClient.class);
    }

}
