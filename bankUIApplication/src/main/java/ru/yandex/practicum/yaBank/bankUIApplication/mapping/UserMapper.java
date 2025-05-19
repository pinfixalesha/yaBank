package ru.yandex.practicum.yaBank.bankUIApplication.mapping;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserListResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.model.CurrencyModel;
import ru.yandex.practicum.yaBank.bankUIApplication.model.UserModel;

@Component
public class UserMapper {
    public UserModel toModel(UserListResponseDto userListResponseDto) {
        if (userListResponseDto == null) {
            return null;
        }

        return UserModel.builder()
                .name(userListResponseDto.getFio())
                .login(userListResponseDto.getLogin())
                .build();
    }
}
