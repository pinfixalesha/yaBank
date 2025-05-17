package ru.yandex.practicum.yaBank.accountsApplication.mapping;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserListResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.entities.User;
import ru.yandex.practicum.yaBank.accountsApplication.utils.DateTimeUtils;

import java.time.LocalDate;

@Component
public class UserMapper {

    public UserResponseDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDto.builder()
                .login(user.getLogin())
                .role(user.getRole())
                .fio(user.getFio())
                .email(user.getEmail())
                .password(user.getPassword())
                .dateOfBirth(user.getDateOfBirth().format(DateTimeUtils.DATE_TIME_FORMATTER))
                .build();
    }

    public UserListResponseDto toExternalDto(User user) {
        if (user == null) {
            return null;
        }

        return UserListResponseDto.builder()
                .login(user.getLogin())
                .fio(user.getFio())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth().format(DateTimeUtils.DATE_TIME_FORMATTER))
                .build();
    }



    public User toEntity(UserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            return null;
        }

        return User.builder()
                .login(userRequestDto.getLogin())
                .fio(userRequestDto.getFio())
                .dateOfBirth(LocalDate.parse(userRequestDto.getDateOfBirth(),DateTimeUtils.DATE_TIME_FORMATTER))
                .password(userRequestDto.getPassword())
                .email(userRequestDto.getEmail())
                .role(userRequestDto.getRole())
                .build();
    }


}
