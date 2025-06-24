package ru.yandex.practicum.yaBank.bankUIApplication.controller;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;
import ru.yandex.practicum.yaBank.bankUIApplication.service.AccountApplicationService;
import org.springframework.beans.factory.annotation.Value;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @Autowired
    private final MeterRegistry meterRegistry;

    @Value("${metricsEnabled:true}")
    private boolean metricsEnabled;

    @Autowired
    private final AccountApplicationService accountApplicationService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Возвращает шаблон login.html
    }

    @GetMapping("/logout")
    public String logoutDefault(HttpServletRequest request, HttpServletResponse response) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("Выход пользователя: " + authentication.getName());
            SecurityContextHolder.clearContext(); // Очищаем контекст безопасности
        }

        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Перенаправляем на главную страницу или страницу входа
        return "redirect:/login?logout";
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
        String emain = request.getParameter("email");
        String dateOfBirthString = request.getParameter("dateOfBirth");

        List<String> errors = new ArrayList<>();
        if ((confirmPassword != null) && (password != null) && (!confirmPassword.equals(password))) {
            errors.add("Пароли не совпадают");
        }

        // Если есть ошибки, возвращаем форму регистрации с сообщениями об ошибках
        if (!errors.isEmpty()) {
            if (metricsEnabled) meterRegistry.counter("user_login", "login", login, "status", "failure").increment();
            attributes.put("errors", errors);
            model.addAllAttributes(attributes);
            return "signup";
        }

        StandardPasswordEncoder encoder = new StandardPasswordEncoder();
        UserDto userDto = UserDto.builder()
                .dateOfBirth(dateOfBirthString)
                .login(login)
                .password(encoder.encode(password))
                .email(emain)
                .fio(fio)
                .role("USER")
                .build();

        HttpResponseDto httpResponseDto=accountApplicationService.registerUser(userDto);

        if (!httpResponseDto.getStatusCode().equals("0")) {
            if (metricsEnabled) meterRegistry.counter("user_login", "login", login, "status", "failure").increment();
            errors.add(httpResponseDto.getStatusMessage());
            attributes.put("errors", errors);
            model.addAllAttributes(attributes);
            return "signup";
        }

        if (metricsEnabled) meterRegistry.counter("user_login", "login", login, "status", "success").increment();
        return "redirect:/login?registeredSuccess";
    }
}
