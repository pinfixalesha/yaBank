package ru.yandex.practicum.yaBank.bankUIApplication.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;

@Service
public class SecurityUserDetailsService  implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StandardPasswordEncoder encoder = new StandardPasswordEncoder();
        String rawPassword = "pass";
        String encodedPassword = encoder.encode(rawPassword);

        return UserDto.builder()
                .id(1L)
                .fio("sss")
                .login("1")
                .password(encodedPassword)
                .role("USER")
                .build();
    }

}
