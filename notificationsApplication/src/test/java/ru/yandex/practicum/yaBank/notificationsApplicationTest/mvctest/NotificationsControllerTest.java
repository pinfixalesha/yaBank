package ru.yandex.practicum.yaBank.notificationsApplicationTest.mvctest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.yaBank.notificationsApplication.NotificationsApplication;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;
import ru.yandex.practicum.yaBank.notificationsApplicationTest.TestSecurityConfig;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {NotificationsApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@AutoConfigureMockMvc
class NotificationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateNotification() throws Exception {
        NotificationDto dto = new NotificationDto("test@example.com", "Hello!", "App");

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated()) // Убедитесь, что статус ответа 201 Created
                .andExpect(jsonPath("$.notificationId", is(1)))
                .andExpect(jsonPath("$.statusMessage", is("Notification send OK")))
                .andExpect(jsonPath("$.statusCode", is("0")));
    }


}
