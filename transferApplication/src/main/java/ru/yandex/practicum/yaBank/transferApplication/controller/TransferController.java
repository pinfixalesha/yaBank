package ru.yandex.practicum.yaBank.transferApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.yaBank.transferApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.TransferOperationDto;
import ru.yandex.practicum.yaBank.transferApplication.service.TransferService;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping
    @Secured("SCOPE_transfer.write")
    public HttpResponseDto transferOperation(@RequestBody TransferOperationDto transferOperationDto) {
        transferService.transferOperation(transferOperationDto);
        return HttpResponseDto.builder()
                .statusMessage("Операция выполнена успешно")
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
