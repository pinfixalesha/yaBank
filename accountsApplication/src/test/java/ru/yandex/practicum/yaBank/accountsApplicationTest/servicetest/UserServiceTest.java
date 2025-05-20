package ru.yandex.practicum.yaBank.accountsApplicationTest.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.yaBank.accountsApplication.AccountsApplication;
import ru.yandex.practicum.yaBank.accountsApplication.dto.HttpResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserRequestDto;
import ru.yandex.practicum.yaBank.accountsApplication.dto.UserResponseDto;
import ru.yandex.practicum.yaBank.accountsApplication.entities.User;
import ru.yandex.practicum.yaBank.accountsApplication.repository.AccountsRepository;
import ru.yandex.practicum.yaBank.accountsApplication.repository.UsersRepository;
import ru.yandex.practicum.yaBank.accountsApplication.service.NotificationService;
import ru.yandex.practicum.yaBank.accountsApplication.service.UserService;
import ru.yandex.practicum.yaBank.accountsApplicationTest.TestSecurityConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AccountsApplication.class, TestSecurityConfig.class})
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @MockitoBean(reset = MockReset.BEFORE)
    private NotificationService notificationService;

    private User user;

    @BeforeEach
    void setUp() {
        accountsRepository.deleteAll();
        usersRepository.deleteAll();

        user = new User();
        user.setLogin("user1");
        user.setPassword("password123");
        user.setFio("Иван Иванов");
        user.setEmail("user1@example.com");
        user.setRole("USER");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setDatetimeCreate(LocalDateTime.now());
        usersRepository.save(user);
    }

    @Test
    void testCreateUser() {
        UserRequestDto request = new UserRequestDto(
                "user2", "password456", "Петр Петров", "USER", "user2@example.com", "1985-05-05"
        );

        HttpResponseDto mockResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("OK")
                .build();

        when(notificationService.sendNotification(anyString(),anyString())).thenReturn(mockResponse);
        Long userId = userService.createUser(request);

        assertNotNull(userId);
        List<User> users = usersRepository.findAll();
        assertEquals(2, users.size());
        assertEquals("user2", users.get(1).getLogin());
        verify(notificationService, times(1)).sendNotification(anyString(),anyString());
    }

    @Test
    void testFindByUsername() {
        UserResponseDto response = userService.findByUsername("user1");

        assertNotNull(response);
        assertEquals("user1", response.getLogin());
        assertEquals("Иван Иванов", response.getFio());
    }

    @Test
    void testEditUser() {
        UserRequestDto request = new UserRequestDto(
                "user1", "newPassword123", "Иван Иванов (обновленный)", "USER", "user1@example.com", "1995-01-01"
        );

        HttpResponseDto mockResponse = HttpResponseDto.builder()
                .statusCode("0")
                .statusMessage("OK")
                .build();

        when(notificationService.sendNotification(anyString(),anyString())).thenReturn(mockResponse);
        Long userId = userService.editUser(request);


        assertNotNull(userId);
        User updatedUser = usersRepository.findUserByLogin("user1").orElse(null);
        assertNotNull(updatedUser);
        assertEquals("Иван Иванов (обновленный)", updatedUser.getFio());
        assertEquals(LocalDate.of(1995, 1, 1), updatedUser.getDateOfBirth());
        verify(notificationService, times(1)).sendNotification(anyString(),anyString());
    }
}
