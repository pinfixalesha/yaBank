package ru.yandex.practicum.yaBank.blockerApplicationTest.mvctest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.yaBank.blockerApplication.BlockerApplication;
import ru.yandex.practicum.yaBank.blockerApplication.dto.BlockerDto;
import ru.yandex.practicum.yaBank.blockerApplicationTest.TestSecurityConfig;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {BlockerApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BlockerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testBlockerOperationAllowed() throws Exception {
        BlockerDto blockerDto = new BlockerDto("XXX", "user123", "deposit", 100.0);

        mockMvc.perform(post("/blocker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(blockerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("0"))
                .andExpect(jsonPath("$.statusMessage").value("Operation access"));
    }

    @Test
    void testHandleException() throws Exception {
        BlockerDto blockerDto = new BlockerDto(null, null, null, null);

        mockMvc.perform(post("/blocker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(blockerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("0"))
                .andExpect(jsonPath("$.statusMessage").isNotEmpty());
    }
}
