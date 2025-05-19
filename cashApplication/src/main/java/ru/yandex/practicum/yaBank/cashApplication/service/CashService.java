package ru.yandex.practicum.yaBank.cashApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.cashApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.BlockerDto;
import ru.yandex.practicum.yaBank.cashApplication.dto.HttpResponseDto;

@Service
public class CashService {

    @Autowired
    private AccountApplicationService accountApplicationService;

    @Autowired
    private BlockerApplicationService blockerApplicationService;

    @Autowired
    private NotificationService notificationService;


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
        notificationService.sendNotification(email, message);
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
        notificationService.sendNotification(email, message);
    }
}
