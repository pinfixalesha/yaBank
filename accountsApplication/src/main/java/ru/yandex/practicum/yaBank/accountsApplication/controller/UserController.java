package ru.yandex.practicum.yaBank.accountsApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.accountsApplication.dto.ChangePasswordRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserListResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("SCOPE_accounts.write")
    public HttpResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        Long userId = userService.createUser(userRequestDto);
        return HttpResponseDto.builder()
                .statusMessage("Create User OK")
                .statusCode("0")
                .build();
    }

    @PostMapping("/changepassword")
    @Secured("SCOPE_accounts.write")
    @ResponseStatus(HttpStatus.OK)
    public HttpResponseDto chengePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        userService.chengePassword(changePasswordRequestDto);
        return HttpResponseDto.builder()
                .statusMessage("Chenge Password User OK")
                .statusCode("0")
                .build();
    }

    @PostMapping("/edit")
    @Secured("SCOPE_accounts.write")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpResponseDto editUser(@RequestBody UserRequestDto userRequestDto) {
        Long userId = userService.editUser(userRequestDto);
        return HttpResponseDto.builder()
                .statusMessage("Edit User OK")
                .statusCode("0")
                .build();
    }

    @GetMapping("/findbylogin")
    @Secured("SCOPE_accounts.read")
    public UserResponseDto findByUsername(@RequestParam("login") String login) {
        UserResponseDto userResponseDto= userService.findByUsername(login);
        if (userResponseDto==null) {
            userResponseDto=UserResponseDto.builder()
                    .statusCode("999")
                    .statusMessage("Пользователь не найден")
                    .build();
        } else  {
            userResponseDto.setStatusCode("0");
            userResponseDto.setStatusMessage("OK");
        }
        return userResponseDto;
    }

    @GetMapping("/all")
    @Secured("SCOPE_accounts.read")
    public List<UserListResponseDto> getUsers() {
        return userService.findAll();
    }

    @ExceptionHandler
    public HttpResponseDto handleException(Exception ex) {
        ex.printStackTrace();
        return HttpResponseDto.builder()
                .statusMessage(ex.getLocalizedMessage())
                .statusCode("999")
                .build();
    }




}
