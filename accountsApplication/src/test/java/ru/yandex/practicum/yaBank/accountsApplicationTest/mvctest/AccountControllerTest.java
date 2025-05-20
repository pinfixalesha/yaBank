package ru.yandex.practicum.yaBank.accountsApplicationTest.mvctest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.yaBank.accountsApplication.AccountsApplication;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.AccountRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.service.AccountsService;
import ru.yandex.practicum.yaBank.accountsApplicationTest.TestSecurityConfig;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {AccountsApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean(reset = MockReset.BEFORE)
    private AccountsService accountsService;

    @Test
    @WithMockUser(authorities = "SCOPE_accounts.write")
    void testCreateAccount() throws Exception {
        AccountRequestDto request = new AccountRequestDto("USD", "user1");
        when(accountsService.addAccount(request)).thenReturn(1L);

        mockMvc.perform(post("/account/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusMessage").value("Create Account User OK"))
                .andExpect(jsonPath("$.statusCode").value("0"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_accounts.read")
    void testGetAccountsByUsername() throws Exception {
        String login = "user1";
        List<AccountDto> mockAccounts = List.of(
                new AccountDto("40215USD1234567890123", "USD", 100.0),
                new AccountDto("40215EUR1234567890123", "EUR", 200.0)
        );
        when(accountsService.findAccountsByLogin(login)).thenReturn(mockAccounts);

        mockMvc.perform(get("/account/all")
                        .param("login", login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("40215USD1234567890123"))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].balance").value(100.0));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_accounts.write")
    void testCashInOperation() throws Exception {
        AccountOperationDto operation = new AccountOperationDto("USD", "user1", 100.0);
        doNothing().when(accountsService).cashIn(operation);

        mockMvc.perform(put("/account/cashIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMessage").value("Операция выполнена успешно"))
                .andExpect(jsonPath("$.statusCode").value("0"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_accounts.read")
    void testGetAccountInfo() throws Exception {
        String currency = "USD";
        String login = "user1";
        AccountDto mockAccount = new AccountDto("40215USD1234567890123", "USD", 100.0);
        when(accountsService.findAccountByLoginAndCurrency(login, currency)).thenReturn(mockAccount);

        mockMvc.perform(get("/account/get")
                        .param("currency", currency)
                        .param("login", login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("40215USD1234567890123"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.balance").value(100.0));
    }
}
