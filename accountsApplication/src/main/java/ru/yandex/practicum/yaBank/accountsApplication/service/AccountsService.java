package ru.yandex.practicum.yaBank.accountsApplication.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.entities.Account;
import ru.yandex.practicum.yaBank.accountsApplication.entities.User;
import ru.yandex.practicum.yaBank.accountsApplication.mapping.AccountMapper;
import ru.yandex.practicum.yaBank.accountsApplication.repository.AccountsRepository;
import ru.yandex.practicum.yaBank.accountsApplication.repository.UsersRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountsService.class);

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final AccountsRepository accountsRepository;

    @Autowired
    private final NotificationService notificationService;

    @Autowired
    private final NotificationProducer notificationProducer;

    @Autowired
    private final AccountMapper accountMapper;

    private static final Random random = new Random();

    public Long addAccount(AccountRequestDto accountRequestDto) {
        log.info("Добавление счета "+accountRequestDto.getCurrency()+" клиенту "+accountRequestDto.getLogin());
        User user = usersRepository.findUserByLogin(accountRequestDto.getLogin()).orElse(null);
        if (user == null) {
            throw new RuntimeException("Пользователь с логином " + accountRequestDto.getLogin() + " не зарегистрирован");
        }

        var foundAccount = accountsRepository.findAccountByUserAndCurrency(
                user,
                accountRequestDto.getCurrency()
        );
        if (foundAccount.isPresent()) {
            throw new RuntimeException("У пользователя " + accountRequestDto.getLogin() + " уже открыт счёт в выбранной валюте");
        }

        var account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(accountRequestDto.getCurrency());
        account.setNumber(generateAccountNumber(accountRequestDto.getCurrency()));
        account.setDatetimeCreate(LocalDateTime.now());
        account.setUser(user);
        Account savedAccount=accountsRepository.save(account);

        //notificationService.sendNotification(user.getEmail(),"Новый счет у пользователя успешно зарегистрирован");
        notificationProducer.sendNotification(user.getEmail(),"Новый счет у пользователя успешно зарегистрирован");
        return savedAccount.getId();
    }

    public static String generateAccountNumber(String currencyCode) {
        int firstPart = 40215;

        int remainingLength = 20 - 5 - currencyCode.length();
        StringBuilder remainingPart = new StringBuilder();
        for (int i = 0; i < remainingLength; i++) {
            remainingPart.append(random.nextInt(10)); // Случайная цифра от 0 до 9
        }

        return firstPart + currencyCode + remainingPart;
    }

    public void cashIn(AccountOperationDto accountOperationDto) {
        log.info("Пополнение счета "+accountOperationDto.getCurrency()+" клиенту "+accountOperationDto.getLogin());
        User user = usersRepository.findUserByLogin(accountOperationDto.getLogin()).orElse(null);
        if (user == null) {
            throw new RuntimeException("Пользователь с логином " + accountOperationDto.getLogin() + " не зарегистрирован");
        }

        Account account = accountsRepository.findAccountByUserAndCurrency(
                user,
                accountOperationDto.getCurrency()).orElse(null);
        if (account == null) {
            throw new RuntimeException("У пользователя " + accountOperationDto.getLogin() + " нет выбранного счета");
        }

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(accountOperationDto.getAmount())));
        accountsRepository.save(account);
    }

    public void cashOut(AccountOperationDto accountOperationDto) {
        log.info("Списание со счета "+accountOperationDto.getCurrency()+" клиенту "+accountOperationDto.getLogin());
        User user = usersRepository.findUserByLogin(accountOperationDto.getLogin()).orElse(null);
        if (user == null) {
            throw new RuntimeException("Пользователь с логином " + accountOperationDto.getLogin() + " не зарегистрирован");
        }

        Account account = accountsRepository.findAccountByUserAndCurrency(
                user,
                accountOperationDto.getCurrency()).orElse(null);
        if (account == null) {
            throw new RuntimeException("У пользователя " + accountOperationDto.getLogin() + " нет выбранного счета");
        }

        account.setBalance(account.getBalance().subtract(BigDecimal.valueOf(accountOperationDto.getAmount())));

        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("У пользователя " + accountOperationDto.getLogin() + " не достаточно денег на счету");
        }
        accountsRepository.save(account);
    }

    public AccountDto findAccountByLoginAndCurrency(String login, String currency) {
        User user = usersRepository.findUserByLogin(login).orElse(null);
        if (user == null) {
            throw new RuntimeException("Пользователь с логином " + login+ " не зарегистрирован");
        }

        Account account = accountsRepository.findAccountByUserAndCurrency(
                user,
                currency).orElse(null);
        if (account == null) {
            throw new RuntimeException("У пользователя " + currency + " нет выбранного счета");
        }

        return accountMapper.toDto(account);
    }

    public List<AccountDto> findAccountsByLogin(String login) {
        User user = usersRepository.findUserByLogin(login).orElse(null);
        if (user == null) {
            throw new RuntimeException("Пользователь с логином " + login+ " не зарегистрирован");
        }

        return accountsRepository.findAllByUser(user).stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }


}
