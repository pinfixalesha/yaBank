package ru.yandex.practicum.yaBank.notificationsApplicationTest.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.yaBank.notificationsApplication.dto.NotificationDto;
import ru.yandex.practicum.yaBank.notificationsApplication.entities.Notification;
import ru.yandex.practicum.yaBank.notificationsApplication.mapping.NotificationMapper;
import ru.yandex.practicum.yaBank.notificationsApplication.repository.NotificationsRepository;
import ru.yandex.practicum.yaBank.notificationsApplication.service.NotificationsService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationsServiceTest {

    @Mock
    private NotificationsRepository notificationsRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationsService notificationsService;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSendNotificationAndReturnId() {
        NotificationDto dto = new NotificationDto("test@example.com", "Hello!", "App");
        Notification entity = new Notification(1L, "test@example.com", "App", "Hello!", null);
        when(notificationMapper.toEntity(dto)).thenReturn(entity);
        when(notificationsRepository.save(entity)).thenReturn(entity);

        Long id = notificationsService.sendNotification(dto);

        assertNotNull(id);
        assertEquals(1L, id);
        verify(notificationsRepository).save(entity);
    }


}
