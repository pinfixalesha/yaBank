package ru.yandex.practicum.yaBank.accountsApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.accountsApplication.service.UserService;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

}
