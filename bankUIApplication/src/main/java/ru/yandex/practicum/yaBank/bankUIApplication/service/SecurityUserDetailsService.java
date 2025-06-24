package ru.yandex.practicum.yaBank.bankUIApplication.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
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
            if (metricsEnabled) meterRegistry.counter("user_login", "login", username, "status", "failure").increment();
            throw new UsernameNotFoundException(userDto.getStatusMessage());
        }
        if (metricsEnabled) meterRegistry.counter("user_login", "login", username, "status", "success").increment();
        return userDto;
    }

}
