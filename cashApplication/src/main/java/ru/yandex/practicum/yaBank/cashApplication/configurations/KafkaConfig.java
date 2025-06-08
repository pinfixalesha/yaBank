package ru.yandex.practicum.yaBank.cashApplication.configurations;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Autowired
    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaTemplate<String, NotificationDto> kafkaTemplate() {
        var producerFactory = new DefaultKafkaProducerFactory<String, NotificationDto>(
                kafkaProperties.buildProducerProperties(null),
                new StringSerializer(),
                new JsonSerializer<NotificationDto>()
        );
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic topicNotifications() {
        return TopicBuilder.name("notifications")
                .partitions(3)
                .replicas(1)
                .compact()
                .build();
    }

}
