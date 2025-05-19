package ru.yandex.practicum.yaBank.bankUIApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserListResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.mapping.AccountMapper;
import ru.yandex.practicum.yaBank.bankUIApplication.mapping.CurrencyMapper;
import ru.yandex.practicum.yaBank.bankUIApplication.mapping.UserMapper;
import ru.yandex.practicum.yaBank.bankUIApplication.model.AccountModel;
import ru.yandex.practicum.yaBank.bankUIApplication.model.CurrencyModel;
import ru.yandex.practicum.yaBank.bankUIApplication.model.UserModel;
import ru.yandex.practicum.yaBank.bankUIApplication.service.AccountApplicationService;
import ru.yandex.practicum.yaBank.bankUIApplication.service.ExchangeApplicationService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class MainController {

    @Autowired
    private ExchangeApplicationService exchangeApplicationService;

    @Autowired
    private AccountApplicationService accountApplicationService;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/main")
    @Secured("ROLE_USER")
    public String mainPage(Model model) {
        UserDto currentUser = getCurrentUser();

        model.addAttribute("login", currentUser.getLogin());
        model.addAttribute("name", currentUser.getFio());
        model.addAttribute("birthdate", currentUser.getDateOfBirth());

        List<CurrencyDto> currencyDtos = exchangeApplicationService.getCurrency();
        List<CurrencyModel> currencyModels = currencyDtos.stream()
                .map(currencyMapper::toModel)
                .collect(Collectors.toList());
        model.addAttribute("currencies", currencyModels);

        List<AccountDto> accountDtos = accountApplicationService.getUserAccountInfo(currentUser.getLogin());
        List<AccountModel> accountModels = accountDtos.stream()
                .map(accountDto -> accountMapper.toModel(accountDto, currencyDtos))
                .collect(Collectors.toList());
        model.addAttribute("accounts", accountModels);

        List<UserListResponseDto> userListResponseDtos = accountApplicationService.getAllUsers();
        List<UserModel> usersModels = userListResponseDtos.stream()
                .map(userMapper::toModel)
                .collect(Collectors.toList());
        model.addAttribute("users", usersModels);

        return "main";
    }


    private UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDto) {
            return (UserDto) authentication.getPrincipal();
        }

        return UserDto.builder()
                .login("guest")
                .password("password")
                .fio("Гость")
                .role("USER")
                .email("guest@example.com")
                .dateOfBirth("1990-01-01")
                .build();
    }
}
