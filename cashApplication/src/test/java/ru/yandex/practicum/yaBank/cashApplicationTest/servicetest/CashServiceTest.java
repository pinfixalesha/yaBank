package ru.yandex.practicum.yaBank.cashApplicationTest.servicetest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.yaBank.cashApplication.CashApplication;
import ru.yandex.practicum.yaBank.cashApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.BlockerDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.cashApplication.service.AccountApplicationService;
import ru.yandex.practicum.yaBank.cashApplication.service.BlockerApplicationService;
import ru.yandex.practicum.yaBank.cashApplication.service.CashService;
import ru.yandex.practicum.yaBank.cashApplication.service.NotificationService;
import ru.yandex.practicum.yaBank.cashApplicationTest.TestSecurityConfig;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {CashApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
public class CashServiceTest {

    @Autowired
    private CashService cashService;

    @MockitoBean(reset = MockReset.BEFORE)
    private AccountApplicationService accountApplicationService;

    @MockitoBean(reset = MockReset.BEFORE)
    private BlockerApplicationService blockerApplicationService;

    @MockitoBean(reset = MockReset.BEFORE)
    private NotificationService notificationService;

    @Test
    void testCashIn_Success() {
        // Arrange
        AccountOperationDto operation = new AccountOperationDto("USD", "user1", 100.0);

        BlockerDto blockerDto = BlockerDto.builder()
                .currency(operation.getCurrency())
                .login(operation.getLogin())
                .action("CASH_IN")
                .amount(operation.getAmount())
                .build();

        HttpResponseDto blockerResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("OK")
                .build();

        HttpResponseDto accountResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("Операция выполнена успешно")
                .build();

        when(blockerApplicationService.checkBlocker(blockerDto)).thenReturn(blockerResponse);
        when(accountApplicationService.cashIn(operation)).thenReturn(accountResponse);
        when(notificationService.sendNotification("user1", "Ваш счет пополнен на 100.0 USD")).thenReturn(null);

        // Act
        cashService.cashIn(operation);

        // Assert
        verify(blockerApplicationService, times(1)).checkBlocker(blockerDto);
        verify(accountApplicationService, times(1)).cashIn(operation);
        verify(notificationService, times(1)).sendNotification("user1", "Ваш счет пополнен на 100.0 USD");
    }

    @Test
    void testCashOut_Success() {
        // Arrange
        AccountOperationDto operation = new AccountOperationDto("USD", "user1", 50.0);

        BlockerDto blockerDto = BlockerDto.builder()
                .currency(operation.getCurrency())
                .login(operation.getLogin())
                .action("CASH_OUT")
                .amount(operation.getAmount())
                .build();

        HttpResponseDto blockerResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("OK")
                .build();

        HttpResponseDto accountResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("Операция выполнена успешно")
                .build();

        when(blockerApplicationService.checkBlocker(blockerDto)).thenReturn(blockerResponse);
        when(accountApplicationService.cashOut(operation)).thenReturn(accountResponse);
        when(notificationService.sendNotification("user1", "С вашего счета выполнено списание на 50.0 USD")).thenReturn(null);

        // Act
        cashService.cashOut(operation);

        // Assert
        verify(blockerApplicationService, times(1)).checkBlocker(blockerDto);
        verify(accountApplicationService, times(1)).cashOut(operation);
        verify(notificationService, times(1)).sendNotification("user1", "С вашего счета выполнено списание на 50.0 USD");
    }

}
