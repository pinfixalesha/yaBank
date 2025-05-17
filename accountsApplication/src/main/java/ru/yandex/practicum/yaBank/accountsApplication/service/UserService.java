package ru.yandex.practicum.yaBank.accountsApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserDto;
import ru.yandex.practicum.yaBank.accountsApplication.mapping.UserMapper;
import ru.yandex.practicum.yaBank.accountsApplication.repository.UsersRepository;
import ru.yandex.practicum.yaBank.accountsApplication.utils.DateTimeUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDto findByUsername(String login) {
        return usersRepository.findUserByLogin(login)
                .map(userMapper::toDto)
                .orElse(null);
    }

    public List<UserDto> findAll() {
        return usersRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateUser(UserDto userDto) {
        Optional.ofNullable(userDto.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Не передан логин"));

        Optional.ofNullable(userDto.getFio())
                .orElseThrow(() -> new IllegalArgumentException("Не передано ФИО"));

        Optional.ofNullable(userDto.getDateOfBirth())
                .orElseThrow(() -> new IllegalArgumentException("Не передана дата рождения клиента"));

        LocalDate dateOfBirth = null;
        try {
            dateOfBirth = LocalDate.parse(userDto.getDateOfBirth(), DateTimeUtils.DATE_TIME_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Использован некорректный формат даты. Корректный формат - yyyy-MM-dd");
        }
        if (!DateTimeUtils.isAdult(dateOfBirth)) {
            throw new IllegalArgumentException("Клиент должен быть старше 18 лет");
        }
    }


}
