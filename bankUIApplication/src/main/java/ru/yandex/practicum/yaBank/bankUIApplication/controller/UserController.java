package ru.yandex.practicum.yaBank.bankUIApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.ChangePasswordRequestDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;
import ru.yandex.practicum.yaBank.bankUIApplication.service.AccountApplicationService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AccountApplicationService accountApplicationService;

    @Autowired
    private MainController mainController;

    @PostMapping("/{login}/editPassword")
    @Secured("ROLE_USER")
    public String editPassword(
            @PathVariable String login,
            @RequestParam String password,
            @RequestParam String confirm_password,
            Model model,
            Principal principal) {

        if (!principal.getName().equals(login)) {
            return "redirect:/logout";
        }

        if (!password.equals(confirm_password)) {
            model.addAttribute("passwordErrors", List.of("Пароли не совпадают"));
            return "main";
        }

        StandardPasswordEncoder encoder = new StandardPasswordEncoder();
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .login(login)
                .password(encoder.encode(password))
                .build();

        HttpResponseDto httpResponseDto=accountApplicationService.changePassword(changePasswordRequestDto);
        if (httpResponseDto.getStatusCode().equals("0")) {
            model.addAttribute("changedPassword", true);
        } else {
            model.addAttribute("passwordErrors",
                    List.of("Ошибка при изменении пароля" + httpResponseDto.getStatusMessage()));
        }

        return mainController.mainPage(model);
    }

    @PostMapping("/{login}/changeInfo")
    @Secured("ROLE_USER")
    public String changeInfo(
            @PathVariable String login,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String birthDate,
            Model model,
            Principal principal) {

        if (!principal.getName().equals(login)) {
            return "redirect:/logout";
        }

        UserDto userDto=getCurrentUser();
        if (userDto==null) {
            return "redirect:/logout";
        }

        UserDto changeUser = UserDto.builder()
                .fio(name)
                .dateOfBirth(birthDate)
                .login(userDto.getLogin())
                .email(userDto.getEmail())
                .build();

        HttpResponseDto httpResponseDto=accountApplicationService.changeInfo(changeUser);
        if (httpResponseDto.getStatusCode().equals("0")) {
            model.addAttribute("userIsUpdated", true);
            userDto.setFio(name);
            userDto.setDateOfBirth(birthDate);
        } else {
            model.addAttribute("userAccountsErrors",
                    List.of("Ошибка при обновлении данных " + httpResponseDto.getStatusMessage()));
        }
        return mainController.mainPage(model);
    }


    private UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDto) {
            return (UserDto) authentication.getPrincipal();
        }

        return null;
    }
}
