package ru.yandex.practicum.yaBank.bankUIApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;

@Service
public class SecurityUserDetailsService  implements UserDetailsService {

    @Autowired
    private AccountApplicationService accountApplicationService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto=accountApplicationService.getUserInfo(username);
        if (!userDto.getStatusCode().equals("0")) {
            throw new UsernameNotFoundException(userDto.getStatusMessage());
        }
        return userDto;
    }

}
