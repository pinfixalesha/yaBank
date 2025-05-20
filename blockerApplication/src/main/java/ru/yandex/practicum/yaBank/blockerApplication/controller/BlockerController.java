package ru.yandex.practicum.yaBank.blockerApplication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.blockerApplication.dto.BlockerDto;
import ru.yandex.practicum.yaBank.blockerApplication.dto.HttpResponseDto;

import java.util.Random;

@RestController
@RequestMapping("/blocker")
public class BlockerController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured("SCOPE_blocker.read")
    public HttpResponseDto checkBlocker(@RequestBody BlockerDto blockerDto) {
        Random random = new Random();
        if ((random.nextInt(100)>90)&&(!blockerDto.getCurrency().equals("XXX"))) {
            return HttpResponseDto.builder()
                    .statusMessage("Operation denied")
                    .statusCode("998")
                    .build();
        }
        return HttpResponseDto.builder()
                .statusMessage("Operation access")
                .statusCode("0")
                .build();
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
