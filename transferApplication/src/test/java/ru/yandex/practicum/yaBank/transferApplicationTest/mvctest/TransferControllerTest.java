package ru.yandex.practicum.yaBank.transferApplicationTest.mvctest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.yaBank.transferApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.transferApplication.dto.TransferOperationDto;
import ru.yandex.practicum.yaBank.transferApplication.service.TransferService;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.yaBank.transferApplication.TransferApplication;
import ru.yandex.practicum.yaBank.transferApplicationTest.TestSecurityConfig;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = {TransferApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean(reset = MockReset.BEFORE)
    private TransferService transferService;

    @Test
    @WithMockUser(authorities = "SCOPE_transfer.write")
    void testTransferOperation() throws Exception {
        TransferOperationDto request = TransferOperationDto.builder()
                .fromLogin("user1")
                .toLogin("user2")
                .fromCurrency("USD")
                .toCurrency("EUR")
                .amount(100.0)
                .build();

        doNothing().when(transferService).transferOperation(request);

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMessage").value("Операция выполнена успешно"))
                .andExpect(jsonPath("$.statusCode").value("0"));

        verify(transferService, times(1)).transferOperation(request);
    }

}
