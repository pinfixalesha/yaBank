package ru.yandex.practicum.yaBank.transferApplicationTest.servicetest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.yaBank.transferApplication.TransferApplication;
import ru.yandex.practicum.yaBank.transferApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.BlockerDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.TransferOperationDto;
import ru.yandex.practicum.yaBank.transferApplication.service.AccountApplicationService;
import ru.yandex.practicum.yaBank.transferApplication.service.BlockerApplicationService;
import ru.yandex.practicum.yaBank.transferApplication.service.ExchangeApplicationService;
import ru.yandex.practicum.yaBank.transferApplication.service.NotificationService;
import ru.yandex.practicum.yaBank.transferApplication.service.TransferService;
import ru.yandex.practicum.yaBank.transferApplicationTest.TestSecurityConfig;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = {TransferApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
public class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @MockitoBean(reset = MockReset.BEFORE)
    private AccountApplicationService accountApplicationService;

    @MockitoBean(reset = MockReset.BEFORE)
    private BlockerApplicationService blockerApplicationService;

    @MockitoBean(reset = MockReset.BEFORE)
    private NotificationService notificationService;

    @MockitoBean(reset = MockReset.BEFORE)
    private ExchangeApplicationService exchangeApplicationService;

    @Test
    void testTransferOperation_Success() {
        TransferOperationDto transferOperation = TransferOperationDto.builder()
                .fromLogin("user1")
                .toLogin("user2")
                .fromCurrency("USD")
                .toCurrency("EUR")
                .amount(100.0)
                .build();

        BlockerDto blockerDtoSender = BlockerDto.builder()
                .currency("USD")
                .login("user1")
                .action("TRANSFER")
                .amount(100.0)
                .build();

        BlockerDto blockerDtoReceiver = BlockerDto.builder()
                .currency("EUR")
                .login("user2")
                .action("TRANSFER")
                .amount(100.0)
                .build();

        HttpResponseDto blockerResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("OK")
                .build();

        AccountOperationDto cashOutDto = AccountOperationDto.builder()
                .currency("USD")
                .login("user1")
                .amount(100.0)
                .build();

        AccountOperationDto cashInDto = AccountOperationDto.builder()
                .currency("EUR")
                .login("user2")
                .amount(200.0) // После конвертации
                .build();

        HttpResponseDto cashOutResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("Операция выполнена успешно")
                .build();

        HttpResponseDto cashInResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("Операция выполнена успешно")
                .build();

        List<CurrencyRateDto> rates = List.of(
                CurrencyRateDto.builder().currency("USD").buy(1.0).sale(1.0).build(),
                CurrencyRateDto.builder().currency("EUR").buy(0.85).sale(0.5).build()
        );

        HttpResponseDto mockResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("OK")
                .build();

        when(blockerApplicationService.checkBlocker(blockerDtoSender)).thenReturn(blockerResponse);
        when(blockerApplicationService.checkBlocker(blockerDtoReceiver)).thenReturn(blockerResponse);
        when(accountApplicationService.cashOut(cashOutDto)).thenReturn(cashOutResponse);
        when(accountApplicationService.cashIn(cashInDto)).thenReturn(cashInResponse);
        when(exchangeApplicationService.getRates()).thenReturn(rates);
        when(notificationService.sendNotification(anyString(), anyString())).thenReturn(mockResponse);

        transferService.transferOperation(transferOperation);

        verify(blockerApplicationService, times(1)).checkBlocker(blockerDtoSender);
        verify(blockerApplicationService, times(1)).checkBlocker(blockerDtoReceiver);
        verify(accountApplicationService, times(1)).cashOut(cashOutDto);
        verify(accountApplicationService, times(1)).cashIn(cashInDto);
        verify(notificationService, times(1)).sendNotification("user1", "Со счета списано 100.0 USD");
        verify(notificationService, times(1)).sendNotification("user2", "Ваш счет пополнен на 200.0 EUR");
    }

}
