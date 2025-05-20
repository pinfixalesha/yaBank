package ru.yandex.practicum.yaBank.bankUIApplicationTest.mvctest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.yaBank.bankUIApplication.BankUIApplication;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.AccountDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.CurrencyDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserDto;
import ru.yandex.practicum.yaBank.bankUIApplication.dto.UserListResponseDto;
import ru.yandex.practicum.yaBank.bankUIApplication.service.AccountApplicationService;
import ru.yandex.practicum.yaBank.bankUIApplication.service.ExchangeApplicationService;
import ru.yandex.practicum.yaBank.bankUIApplicationTest.TestSecurityConfig;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {BankUIApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean(reset = MockReset.BEFORE)
    private ExchangeApplicationService exchangeApplicationService;

    @MockitoBean(reset = MockReset.BEFORE)
    private AccountApplicationService accountApplicationService;

    @BeforeEach
    void setUp() {
        // Настройка SecurityContext для имитации аутентифицированного пользователя
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn(
                UserDto.builder()
                        .login("user1")
                        .fio("Иван Иванов")
                        .dateOfBirth("1990-01-01")
                        .build()
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void testMainPage() throws Exception {
        // Arrange
        List<CurrencyDto> currencyDtos = List.of(
                CurrencyDto.builder().currency("USD").title("Доллар").build(),
                CurrencyDto.builder().currency("EUR").title("Рубль").build()
        );

        List<AccountDto> accountDtos = List.of(
                AccountDto.builder().accountNumber("1234567890").currency("USD").balance(1000.0).build(),
                AccountDto.builder().accountNumber("0987654321").currency("EUR").balance(500.0).build()
        );

        List<UserListResponseDto> userListResponseDtos = List.of(
                UserListResponseDto.builder().login("user1").fio("Иван Иванов").email("user1@example.com").build(),
                UserListResponseDto.builder().login("user2").fio("Петр Петров").email("user2@example.com").build()
        );

        when(exchangeApplicationService.getCurrency()).thenReturn(currencyDtos);
        when(accountApplicationService.getUserAccountInfo("user1")).thenReturn(accountDtos);
        when(accountApplicationService.getAllUsers()).thenReturn(userListResponseDtos);

        mockMvc.perform(get("/main"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("login"))
                .andExpect(model().attributeExists("name"))
                .andExpect(model().attributeExists("birthdate"))
                .andExpect(model().attributeExists("currencies"))
                .andExpect(model().attributeExists("accounts"))
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("main"));

        verify(exchangeApplicationService, times(1)).getCurrency();
        verify(accountApplicationService, times(1)).getUserAccountInfo("guest");
        verify(accountApplicationService, times(1)).getAllUsers();
    }
}
