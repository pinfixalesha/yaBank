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
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountRequestDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.ChangePasswordRequestDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.TransferOperationDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;
import ru.yandex.practicum.yaBank.bankUIApplication.service.AccountApplicationService;
import ru.yandex.practicum.yaBank.bankUIApplication.service.CashApplicationService;
import ru.yandex.practicum.yaBank.bankUIApplication.service.TransferApplicationService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AccountApplicationService accountApplicationService;

    @Autowired
    private CashApplicationService cashApplicationService;

    @Autowired
    private TransferApplicationService transferApplicationService;

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

    @PostMapping("/{login}/accounts")
    @Secured("ROLE_USER")
    public String openAccount(
            @PathVariable String login,
            @RequestParam String currency,
            Model model,
            Principal principal) {

        AccountRequestDto requestDto = new AccountRequestDto();
        requestDto.setCurrency(currency);
        requestDto.setLogin(login);

        HttpResponseDto httpResponseDto = accountApplicationService.accountAdd(requestDto);

        if (httpResponseDto.getStatusCode().equals("0")) {
            model.addAttribute("accountIsOpen", true);
        } else {
            model.addAttribute("accountErrors",
                    List.of("Ошибка при создании счета " + httpResponseDto.getStatusMessage()));
        }
        return mainController.mainPage(model);
    }


    @PostMapping("/{login}/cash")
    @Secured("ROLE_USER")
    public String handleCashOperation(
            @PathVariable String login,
            @RequestParam String currency,
            @RequestParam double value,
            @RequestParam String action,
            Model model) {

        AccountOperationDto accountOperationDto = AccountOperationDto.builder()
                .login(login)
                .currency(currency)
                .amount(value)
                .build();

        HttpResponseDto httpResponseDto;
        if ("PUT".equalsIgnoreCase(action)) {
            httpResponseDto = cashApplicationService.cashIn(accountOperationDto);
        } else if ("GET".equalsIgnoreCase(action)) {
            httpResponseDto = cashApplicationService.cashOut(accountOperationDto);
        } else {
            model.addAttribute("cashErrors", "Неверное действие: " + action);
            return mainController.mainPage(model);
        }

        if (httpResponseDto.getStatusCode().equals("0")) {
            model.addAttribute("cashIsOK", true);
        } else {
            model.addAttribute("cashErrors",
                    List.of("Ошибка операции " + httpResponseDto.getStatusMessage()));
        }
        return mainController.mainPage(model);
    }

    @PostMapping("/{login}/transfer")
    @Secured("ROLE_USER")
    public String handleTransferOperation(
            @PathVariable String login,
            @RequestParam(name = "from_currency" ) String fromCurrency,
            @RequestParam(name = "to_currency") String toCurrency,
            @RequestParam(name = "to_login") String toLogin,
            @RequestParam double value,
            Model model) {
        // Подготовка DTO для перевода
        TransferOperationDto transferOperationDto = TransferOperationDto.builder()
                .fromLogin(login)
                .toLogin(toLogin)
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .amount(value)
                .build();

        HttpResponseDto httpResponseDto = transferApplicationService.transfer(transferOperationDto);

        if ("0".equals(httpResponseDto.getStatusCode())) {
            if (transferOperationDto.getToLogin().equals(transferOperationDto.getFromLogin())) {
                model.addAttribute("transferIsOK", true);
            } else {
                model.addAttribute("transferOtherIsOK", true);
            }
        } else {
            if (transferOperationDto.getToLogin().equals(transferOperationDto.getFromLogin())) {
                model.addAttribute("transferErrors",
                        List.of("Ошибка операции: " + httpResponseDto.getStatusMessage()));
            } else {
                model.addAttribute("transferOtherErrors",
                        List.of("Ошибка операции: " + httpResponseDto.getStatusMessage()));
            }
        }

        return mainController.mainPage(model);
    }

}
