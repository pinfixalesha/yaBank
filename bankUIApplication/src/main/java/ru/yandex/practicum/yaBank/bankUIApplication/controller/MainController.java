package ru.yandex.practicum.yaBank.bankUIApplication.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;

import java.util.List;

@Controller
@RequestMapping
public class MainController {

    @GetMapping("/main")
    @Secured("ROLE_USER")
    public String mainPage(Model model) {
        UserDto currentUser = getCurrentUser();

        model.addAttribute("login", currentUser.getLogin());
        model.addAttribute("name", currentUser.getFio());
        model.addAttribute("birthdate", currentUser.getDateOfBirth());
        /*
        // Пример валют
         model.addAttribute("currencies", List.of(
                new Currency("RUB", "Российский рубль"),
                new Currency("USD", "Доллар США"),
                new Currency("EUR", "Евро")
        ));

        // Пример счетов пользователя
        model.addAttribute("accounts", List.of(
                new Account("RUB", "Счет №1", 10000),
                new Account("USD", "Счет №2", 500)
        ));
        */
        return "main";
    }


    private UserDto getCurrentUser() {
        // Получаем объект аутентификации из SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Предполагается, что объект UserDetails хранится в аутентификации
        if (authentication != null && authentication.getPrincipal() instanceof UserDto) {
            return (UserDto) authentication.getPrincipal();
        }

        // Если пользователь не аутентифицирован, возвращаем заглушку
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
