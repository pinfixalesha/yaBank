package ru.yandex.practicum.yaBank.notificationsApplicationTest.servicetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;
import ru.yandex.practicum.yaBank.notificationsApplication.entities.Notification;
import ru.yandex.practicum.yaBank.notificationsApplication.mapping.NotificationMapper;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationMapperTest {

    @InjectMocks
    private NotificationMapper notificationMapper;

    @Test
    void shouldMapNotificationDtoToEntity() {
        NotificationDto dto = new NotificationDto("test@example.com", "Hello!", "App");

        Notification entity = notificationMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("test@example.com", entity.getEmail());
        assertEquals("App", entity.getApplicationName());
        assertEquals("Hello!", entity.getMessage());
        assertNotNull(entity.getDateTimeCreate());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        Notification entity = notificationMapper.toEntity(null);

        assertNull(entity);
    }
}
