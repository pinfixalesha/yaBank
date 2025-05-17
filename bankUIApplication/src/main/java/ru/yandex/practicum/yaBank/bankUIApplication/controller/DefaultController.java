package ru.yandex.practicum.yaBank.bankUIApplication.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class DefaultController {

    @GetMapping
    public String mainDefault() {
        return "redirect:/main";
    }

}
