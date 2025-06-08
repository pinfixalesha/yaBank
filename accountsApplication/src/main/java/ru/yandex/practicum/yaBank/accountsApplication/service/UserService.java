package ru.yandex.practicum.yaBank.accountsApplication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.accountsApplication.dto.ChangePasswordRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserListResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.entities.User;
import ru.yandex.practicum.yaBank.accountsApplication.mapping.UserMapper;
import ru.yandex.practicum.yaBank.accountsApplication.repository.UsersRepository;
import ru.yandex.practicum.yaBank.accountsApplication.utils.DateTimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final NotificationService notificationService;

    @Autowired
    private final NotificationProducer notificationProducer;

    public UserResponseDto findByUsername(String login) {
        return usersRepository.findUserByLogin(login)
                .map(userMapper::toDto)
                .orElse(null);
    }

    public List<UserListResponseDto> findAll() {
        return usersRepository.findAll().stream()
                .map(userMapper::toExternalDto)
                .collect(Collectors.toList());
    }

    public Long createUser(UserRequestDto userRequestDto) {
        validateUser(userRequestDto);

        var foundUser = usersRepository.findUserByLogin(userRequestDto.getLogin());
        if (foundUser.isPresent()) {
            throw new RuntimeException("Пользователь с логином " + userRequestDto.getLogin() + " уже зарегистрирован");
        }
        User user = userMapper.toEntity(userRequestDto);
        if (userRequestDto.getPassword() == null) {
            throw new IllegalArgumentException("Не передан пароль");
        }
        user.setDatetimeCreate(LocalDateTime.now());
        User savedUser = usersRepository.save(user);
        Long userId=savedUser.getId();
        //notificationService.sendNotification(userRequestDto.getEmail(),"Новый пользователь успешно зарегистрирован");
        notificationProducer.sendNotification(userRequestDto.getEmail(),"Новый пользователь успешно зарегистрирован");
        return userId;
    }

    public void chengePassword(ChangePasswordRequestDto changePasswordRequestDto) {

        var foundUser = usersRepository.findUserByLogin(changePasswordRequestDto.getLogin());
        if (!foundUser.isPresent()) {
            throw new RuntimeException("Пользователь с логином " + changePasswordRequestDto.getLogin() + " не зарегистрирован");
        }
        User user = foundUser.get();
        user.setPassword(changePasswordRequestDto.getPassword());
        usersRepository.save(user);
        //notificationService.sendNotification(user.getEmail(),"Пароль пользователя успешно изменен");
        notificationProducer.sendNotification(user.getEmail(),"Пароль пользователя успешно изменен");
    }

    public Long editUser(UserRequestDto userRequestDto) {
        validateUser(userRequestDto);

        var foundUser = usersRepository.findUserByLogin(userRequestDto.getLogin());
        if (!foundUser.isPresent()) {
            throw new RuntimeException("Пользователь с логином " + userRequestDto.getLogin() + " не зарегистрирован");
        }
        User user = foundUser.get();
        user.setFio(userRequestDto.getFio());
        user.setDateOfBirth(LocalDate.parse(userRequestDto.getDateOfBirth(),DateTimeUtils.DATE_TIME_FORMATTER));
        User savedUser = usersRepository.save(user);
        //notificationService.sendNotification(user.getEmail(),"Пользователь успешно отредактирован");
        notificationProducer.sendNotification(user.getEmail(),"Пользователь успешно отредактирован");
        return savedUser.getId();
    }


    private void validateUser(UserRequestDto userRequestDto) {
        Optional.ofNullable(userRequestDto.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Не передан логин"));

        Optional.ofNullable(userRequestDto.getFio())
                .orElseThrow(() -> new IllegalArgumentException("Не передано ФИО"));

        Optional.ofNullable(userRequestDto.getDateOfBirth())
                .orElseThrow(() -> new IllegalArgumentException("Не передана дата рождения клиента"));

        LocalDate dateOfBirth = null;
        try {
            dateOfBirth = LocalDate.parse(userRequestDto.getDateOfBirth(), DateTimeUtils.DATE_TIME_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Использован некорректный формат даты. Корректный формат - yyyy-MM-dd");
        }
        if (!DateTimeUtils.isAdult(dateOfBirth)) {
            throw new IllegalArgumentException("Клиент должен быть старше 18 лет");
        }
    }


}
