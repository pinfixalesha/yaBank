package ru.yandex.practicum.yaBank.bankUIApplication.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Возвращает шаблон login.html
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup"; // Возвращает шаблон login.html
    }

    @PostMapping("/signup")
    public String registerUser(HttpServletRequest request, Model model) {
        // Получение CSRF-токена
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("_csrf", csrfToken);

        // Получение данных формы
        String confirmPassword = request.getParameter("confirm_password");
        String password = request.getParameter("password");
        String login = request.getParameter("login");
        String fio = request.getParameter("name");
        String dateOfBirthString = request.getParameter("dateOfBirth");

        List<String> errors = new ArrayList<>();
        if ((confirmPassword != null) && (password != null) && (!confirmPassword.equals(password))) {
            errors.add("Пароли не совпадают");
        }

        // Если есть ошибки, возвращаем форму регистрации с сообщениями об ошибках
        if (!errors.isEmpty()) {
            attributes.put("errors", errors);
            model.addAllAttributes(attributes);
            return "signup";
        }

//        // Регистрация пользователя
//        UserDto userDto = UserDto.builder()
//                .dateOfBirth(dateOfBirthString)
//                .login(login)
//                .password(password)
//                .lastName(fioDelimetedBySpace.split(" ")[0])
//                .firstName(fioDelimetedBySpace.split(" ")[1])
//                .patronymic(fioDelimetedBySpace.split(" ").length > 2 ? fioDelimetedBySpace.split(" ")[2] : null)
//                .build();
//
//        String response = userDetailsService.registerUser(userDto);

//        if (!response.equals("OK")) {
//            attributes.put("errors", response);
//            model.addAllAttributes(attributes);
//            return "signup";
//        }

        return "redirect:/login?registeredSuccess";
    }
}
