package ru.yandex.practicum.yaBank.accountsApplicationTest.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.yaBank.accountsApplication.AccountsApplication;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.entities.Account;
import ru.yandex.practicum.yaBank.accountsApplication.entities.User;
import ru.yandex.practicum.yaBank.accountsApplication.repository.AccountsRepository;
import ru.yandex.practicum.yaBank.accountsApplication.repository.UsersRepository;
import ru.yandex.practicum.yaBank.accountsApplication.service.AccountsService;
import ru.yandex.practicum.yaBank.accountsApplication.service.NotificationService;
import ru.yandex.practicum.yaBank.accountsApplicationTest.TestSecurityConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AccountsApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
public class AccountsServiceTest {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @MockitoBean(reset = MockReset.BEFORE)
    private NotificationService notificationService;

    private User user;

    @BeforeEach
    void setUp() {
        accountsRepository.deleteAll();
        usersRepository.deleteAll();

        user = new User();
        user.setLogin("user1");
        user.setPassword("password123");
        user.setFio("Иван Иванов");
        user.setEmail("user1@example.com");
        user.setRole("USER");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setDatetimeCreate(LocalDateTime.now());
        usersRepository.save(user);
    }

    @Test
    void testAddAccount() {
        AccountRequestDto request = new AccountRequestDto("USD", "user1");

        HttpResponseDto mockResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("OK")
                .build();

        when(notificationService.sendNotification(anyString(),anyString())).thenReturn(mockResponse);
        Long accountId = accountsService.addAccount(request);

        assertNotNull(accountId);
        List<Account> accounts = accountsRepository.findAllByUser(user);
        assertEquals(1, accounts.size());
        assertEquals("USD", accounts.get(0).getCurrency());
        assertEquals(0, accounts.get(0).getBalance().doubleValue());

        verify(notificationService, times(1)).sendNotification(anyString(),anyString());
    }

    @Test
    void testCashIn() {
        Account account = new Account();
        account.setUser(user);
        account.setCurrency("USD");
        account.setNumber("40215USD123456789012");
        account.setBalance(BigDecimal.ZERO);
        account.setDatetimeCreate(LocalDateTime.now());
        accountsRepository.save(account);

        AccountOperationDto operation = new AccountOperationDto("USD", "user1", 100.0);

        accountsService.cashIn(operation);

        List<Account> accounts = accountsRepository.findAllByUser(user);
        assertEquals(1, accounts.size());
        assertEquals(100, accounts.get(0).getBalance().doubleValue());
    }

    @Test
    void testCashOutFalure() {
        Account account = new Account();
        account.setUser(user);
        account.setCurrency("USD");
        account.setNumber("40215USD123456789012");
        account.setBalance(BigDecimal.valueOf(50.0));
        account.setDatetimeCreate(LocalDateTime.now());
        accountsRepository.save(account);

        AccountOperationDto operation = new AccountOperationDto("USD", "user1", 100.0);

        Exception exception = assertThrows(RuntimeException.class, () -> accountsService.cashOut(operation));
        assertEquals("У пользователя user1 не достаточно денег на счету", exception.getMessage());
    }

    @Test
    void testFindAccountsByUsername() {
        Account account1 = new Account();
        account1.setUser(user);
        account1.setCurrency("USD");
        account1.setNumber("40215USD123456789012");
        account1.setBalance(BigDecimal.ZERO);
        account1.setDatetimeCreate(LocalDateTime.now());

        Account account2 = new Account();
        account2.setUser(user);
        account2.setCurrency("EUR");
        account2.setNumber("40215EUR123456789012");
        account2.setBalance(BigDecimal.ZERO);
        account2.setDatetimeCreate(LocalDateTime.now());

        accountsRepository.saveAll(List.of(account1, account2));

        List<AccountDto> accounts = accountsService.findAccountsByLogin("user1");

        assertEquals(2, accounts.size());
        assertEquals("USD", accounts.get(0).getCurrency());
        assertEquals("EUR", accounts.get(1).getCurrency());
    }
}
