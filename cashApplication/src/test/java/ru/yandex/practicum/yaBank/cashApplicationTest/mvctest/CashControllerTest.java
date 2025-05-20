package ru.yandex.practicum.yaBank.cashApplicationTest.mvctest;

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
import ru.yandex.practicum.yaBank.cashApplication.CashApplication;
import ru.yandex.practicum.yaBank.cashApplication.dto.AccountOperationDto;
import ru.yandex.practicum.yaBank.cashApplication.service.CashService;
import ru.yandex.practicum.yaBank.cashApplicationTest.TestSecurityConfig;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {CashApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CashControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean(reset = MockReset.BEFORE)
    private CashService cashService;

    @Test
    @WithMockUser(authorities = "SCOPE_cash.write")
    void testCashInOperation() throws Exception {
        AccountOperationDto request = new AccountOperationDto("USD", "user1", 100.0);
        doNothing().when(cashService).cashIn(request);

        mockMvc.perform(post("/cash/in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMessage").value("Операция выполнена успешно"))
                .andExpect(jsonPath("$.statusCode").value("0"));

        verify(cashService, times(1)).cashIn(request);
    }

    @Test
    @WithMockUser(authorities = "SCOPE_cash.write")
    void testCashOutOperation() throws Exception {
        AccountOperationDto request = new AccountOperationDto("USD", "user1", 50.0);
        doNothing().when(cashService).cashOut(request);

        mockMvc.perform(post("/cash/out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMessage").value("Операция выполнена успешно"))
                .andExpect(jsonPath("$.statusCode").value("0"));

        verify(cashService, times(1)).cashOut(request);
    }
}
