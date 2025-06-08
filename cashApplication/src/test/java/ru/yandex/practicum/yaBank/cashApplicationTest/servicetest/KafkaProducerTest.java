package ru.yandex.practicum.yaBank.cashApplicationTest.servicetest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.yandex.practicum.yaBank.cashApplication.CashApplication;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;
import ru.yandex.practicum.yaBank.cashApplicationTest.TestSecurityConfig;


import java.util.UUID;

@SpringBootTest(classes = {CashApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@EmbeddedKafka(topics = {"notifications"})
public class KafkaProducerTest {

    @Autowired
    private KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Test
    public void testProcessor(){
        NotificationDto notificationDto = NotificationDto.builder()
                .email("test@mail.ru")
                .message("Тестовое сообщение")
                .application("account")
                .build();

        String key = UUID.randomUUID().toString();
        kafkaTemplate.send("notifications", key, notificationDto);

    }


}
