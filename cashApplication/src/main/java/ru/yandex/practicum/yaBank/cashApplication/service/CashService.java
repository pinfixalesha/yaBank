package ru.yandex.practicum.yaBank.cashApplication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.cashApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.BlockerDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.HttpResponseDto;

@Service
@RequiredArgsConstructor
public class CashService {

    @Autowired
    private final AccountApplicationService accountApplicationService;

    @Autowired
    private final  BlockerApplicationService blockerApplicationService;

    @Autowired
    private final NotificationService notificationService;

    @Autowired
    private final NotificationProducer notificationProducer;

    public void cashIn(AccountOperationDto accountOperationDto) {
        BlockerDto blockerDto = BlockerDto.builder()
                .currency(accountOperationDto.getCurrency())
                .login(accountOperationDto.getLogin())
                .action("CASH_IN")
                .amount(accountOperationDto.getAmount())
                .build();

        HttpResponseDto blockerResponse = blockerApplicationService.checkBlocker(blockerDto);
        if (!blockerResponse.getStatusCode().equals("0")) {
            throw new RuntimeException(blockerResponse.getStatusMessage());
        }

        HttpResponseDto accountResponse = accountApplicationService.cashIn(accountOperationDto);
        if (!accountResponse.getStatusCode().equals("0")) {
            throw new RuntimeException(accountResponse.getStatusMessage());
        }

        String email = accountOperationDto.getLogin();
        String message = "Ваш счет пополнен на " + accountOperationDto.getAmount() + " " + accountOperationDto.getCurrency();
        //notificationService.sendNotification(email, message);
        notificationProducer.sendNotification(email, message);
    }

    public void cashOut(AccountOperationDto accountOperationDto) {
        BlockerDto blockerDto = BlockerDto.builder()
                .currency(accountOperationDto.getCurrency())
                .login(accountOperationDto.getLogin())
                .action("CASH_OUT")
                .amount(accountOperationDto.getAmount())
                .build();

        HttpResponseDto blockerResponse = blockerApplicationService.checkBlocker(blockerDto);
        if (!blockerResponse.getStatusCode().equals("0")) {
            throw new RuntimeException(blockerResponse.getStatusMessage());
        }

        HttpResponseDto accountResponse = accountApplicationService.cashOut(accountOperationDto);
        if (!accountResponse.getStatusCode().equals("0")) {
            throw new RuntimeException(accountResponse.getStatusMessage());
        }

        String email = accountOperationDto.getLogin();
        String message = "С вашего счета выполнено списание на " + accountOperationDto.getAmount() + " " + accountOperationDto.getCurrency();
        //notificationService.sendNotification(email, message);
        notificationProducer.sendNotification(email, message);
    }
}
