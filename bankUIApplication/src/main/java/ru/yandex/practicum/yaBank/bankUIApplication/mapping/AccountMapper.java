package ru.yandex.practicum.yaBank.bankUIApplication.mapping;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.bankUIApplication.model.AccountModel;
import ru.yandex.practicum.yaBank.bankUIApplication.model.CurrencyModel;

import java.util.List;
import java.util.Optional;

@Component
public class AccountMapper {
    public AccountModel toModel(AccountDto accountDto, List<CurrencyDto> currencyDtos) {
        if (accountDto == null) {
            return null;
        }

        String currencyTitle = findCurrencyTitleByCode(currencyDtos, accountDto.getCurrency());

        return AccountModel.builder()
                .accountNumber(accountDto.getAccountNumber())
                .balance(accountDto.getBalance())
                .currencyTitle(currencyTitle)
                .build();
    }

    private String findCurrencyTitleByCode(List<CurrencyDto> currencyDtos, String currencyCode) {
        if (currencyCode == null || currencyDtos == null) {
            return null;
        }

        Optional<CurrencyDto> currencyDtoOptional = currencyDtos.stream()
                .filter(currencyDto -> currencyCode.equals(currencyDto.getCurrency()))
                .findFirst();

        return currencyDtoOptional.map(CurrencyDto::getTitle).orElse("");
    }
}
