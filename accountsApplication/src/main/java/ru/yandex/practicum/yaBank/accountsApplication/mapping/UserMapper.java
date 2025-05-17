package ru.yandex.practicum.yaBank.accountsApplication.mapping;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserDto;
import ru.yandex.practicum.yaBank.accountsApplication.entities.User;
import ru.yandex.practicum.yaBank.accountsApplication.utils.DateTimeUtils;

import java.time.format.DateTimeFormatter;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole())
                .fio(user.getFio())
                .password(user.getPassword())
                .dateOfBirth(user.getDateOfBirth().format(DateTimeUtils.DATE_TIME_FORMATTER))
                .build();
    }

}
