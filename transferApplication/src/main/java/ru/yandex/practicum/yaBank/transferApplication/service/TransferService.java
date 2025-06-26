package ru.yandex.practicum.yaBank.transferApplication.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.transferApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.BlockerDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.CurrencyRateDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.TransferOperationDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private final AccountApplicationService accountApplicationService;

    @Autowired
    private final BlockerApplicationService blockerApplicationService;

    @Autowired
    private final NotificationService notificationService;

    @Autowired
    private final NotificationProducer notificationProducer;

    @Autowired
    private final ExchangeApplicationService exchangeApplicationService;


    public void transferOperation(TransferOperationDto transferOperationDto) {
        BlockerDto blockerDto = BlockerDto.builder()
                .currency(transferOperationDto.getFromCurrency())
                .login(transferOperationDto.getFromLogin())
                .action("TRANSFER")
                .amount(transferOperationDto.getAmount())
                .build();

        HttpResponseDto blockerResponse = blockerApplicationService.checkBlocker(blockerDto);
        if (!blockerResponse.getStatusCode().equals("0")) {
            throw new RuntimeException(blockerResponse.getStatusMessage());
        }

        blockerDto = BlockerDto.builder()
                .currency(transferOperationDto.getToCurrency())
                .login(transferOperationDto.getToLogin())
                .action("TRANSFER")
                .amount(transferOperationDto.getAmount())
                .build();

        blockerResponse = blockerApplicationService.checkBlocker(blockerDto);
        if (!blockerResponse.getStatusCode().equals("0")) {
            throw new RuntimeException(blockerResponse.getStatusMessage());
        }

        String fromCurrency = transferOperationDto.getFromCurrency();
        String toCurrency = transferOperationDto.getToCurrency();
        Double amount = transferOperationDto.getAmount();

        //Списание средств со счета отправителя
        AccountOperationDto cashOutDto = AccountOperationDto.builder()
                .currency(fromCurrency)
                .login(transferOperationDto.getFromLogin())
                .amount(amount)
                .build();

        HttpResponseDto cashOutResponse = accountApplicationService.cashOut(cashOutDto);
        if (!cashOutResponse.getStatusCode().equals("0")) {
            throw new RuntimeException(cashOutResponse.getStatusMessage());
        }

        //Конвертация суммы, если валюты различаются
        double convertedAmount = convertAmount(fromCurrency, toCurrency, amount);
        ;

        //Зачисление средств на счет получателя
        AccountOperationDto cashInDto = AccountOperationDto.builder()
                .currency(toCurrency)
                .login(transferOperationDto.getToLogin())
                .amount(convertedAmount)
                .build();

        HttpResponseDto cashInResponse = accountApplicationService.cashIn(cashInDto);
        if (!cashInResponse.getStatusCode().equals("0")) {
            AccountOperationDto refundDto = AccountOperationDto.builder()
                    .currency(fromCurrency)
                    .login(transferOperationDto.getFromLogin())
                    .amount(amount)
                    .build();

            HttpResponseDto refundResponse = accountApplicationService.cashIn(refundDto);
            if (!refundResponse.getStatusCode().equals("0")) {
                throw new RuntimeException(refundResponse.getStatusMessage());
            }
            throw new RuntimeException(cashInResponse.getStatusMessage());
        }

        String emailFrom = cashOutDto.getLogin();
        String messageFrom = "Со счета списано " + cashOutDto.getAmount() + " " + cashOutDto.getCurrency();
        //notificationService.sendNotification(emailFrom, messageFrom);
        notificationProducer.sendNotification(emailFrom, messageFrom);

        String emailTo = cashInDto.getLogin();
        String messageTo = "Ваш счет пополнен на " + cashInDto.getAmount() + " " + cashInDto.getCurrency();
        //notificationService.sendNotification(emailTo, messageTo);
        notificationProducer.sendNotification(emailTo, messageTo);
    }

    private double convertAmount(String fromCurrency, String toCurrency, double amount) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        List<CurrencyRateDto> rates = exchangeApplicationService.getRates();

        Optional<CurrencyRateDto> fromRate = rates.stream()
                .filter(rate -> rate.getCurrency().equals(fromCurrency))
                .findFirst();

        Optional<CurrencyRateDto> toRate = rates.stream()
                .filter(rate -> rate.getCurrency().equals(toCurrency))
                .findFirst();

        if (fromRate.isEmpty() || toRate.isEmpty()) {
            throw new IllegalArgumentException("Не удалось получить курсы валют для конвертации.");
        }

        double fromBuyRate = fromRate.get().getBuy(); // Курс продажи fromCurrency
        double toSaleRate = toRate.get().getSale();       // Курс покупки toCurrency

        double result = (amount * fromBuyRate) / toSaleRate;
        log.info("Сумма конвертации с " + fromCurrency + " в " + toCurrency + " суммы " + amount + "составляет " + result);

        return result;
    }
}
