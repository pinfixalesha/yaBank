package ru.yandex.practicum.yaBank.blockerApplication.controller;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
@RequestMapping("/blocker")
public class BlockerController {

    private static final Logger log = LoggerFactory.getLogger(BlockerController.class);

    @Autowired
    private final MeterRegistry meterRegistry;

    @Value("${metricsEnabled:true}")
    private boolean metricsEnabled;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured("SCOPE_blocker.read")
    public HttpResponseDto checkBlocker(@RequestBody BlockerDto blockerDto) {
        Random random = new Random();
        if ((random.nextInt(100)>90)&&(!blockerDto.getCurrency().equals("XXX"))) {
            log.info("Операция "+blockerDto.getAction()+" заблокирована");
            if (metricsEnabled) meterRegistry.counter("blocker_operation").increment();
            return HttpResponseDto.builder()
                    .statusMessage("Operation denied")
                    .statusCode("998")
                    .build();
        }
        log.info("Операция "+blockerDto.getAction()+" разрешена");
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
