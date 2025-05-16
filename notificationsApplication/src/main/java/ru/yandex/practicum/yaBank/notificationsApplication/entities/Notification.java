package ru.yandex.practicum.yaBank.notificationsApplication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notifications_log", schema = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "application_name", nullable = false)
    private String applicationName;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "datetime_create", nullable = false)
    private LocalDateTime dateTimeCreate;

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", message='" + message + '\'' +
                ", dateTimeCreate=" + dateTimeCreate +
                '}';
    }
}
