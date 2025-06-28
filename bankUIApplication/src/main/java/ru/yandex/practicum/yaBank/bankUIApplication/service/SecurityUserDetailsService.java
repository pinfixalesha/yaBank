package ru.yandex.practicum.yaBank.bankUIApplication.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService  implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(SecurityUserDetailsService.class);

    @Autowired
    private final AccountApplicationService accountApplicationService;

    @Autowired
    private final MeterRegistry meterRegistry;

    @Value("${metricsEnabled:true}")
    private boolean metricsEnabled;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto=accountApplicationService.getUserInfo(username);
        if (!userDto.getStatusCode().equals("0")) {
            log.error("Авторизация пользователя не выполнена "+username);
            if (metricsEnabled) meterRegistry.counter("user_login", "login", username, "status", "failure").increment();
            throw new UsernameNotFoundException(userDto.getStatusMessage());
        }
        if (metricsEnabled) meterRegistry.counter("user_login", "login", username, "status", "success").increment();
        return userDto;
    }

}
