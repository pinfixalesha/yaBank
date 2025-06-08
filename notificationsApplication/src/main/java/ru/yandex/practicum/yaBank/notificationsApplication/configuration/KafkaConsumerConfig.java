package ru.yandex.practicum.yaBank.notificationsApplication.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;


@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Autowired
    private final KafkaProperties kafkaProperties;

    @Bean
    public DefaultKafkaConsumerFactory<String, NotificationDto> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(null),
                new StringDeserializer(),
                new JsonDeserializer<>(new com.fasterxml.jackson.core.type.TypeReference<NotificationDto>() {})
        );
    }
}
