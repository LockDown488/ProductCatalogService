package ru.kopanev.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kopanev.enums.Action;

import java.time.LocalDateTime;

/**
 * Представляет событие аудита в системе.
 * Записывает информацию о действиях пользователей для отслеживания активности.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private Long id;
    private String username;
    private Action action;
    private String details;
    private LocalDateTime timestamp;

    public Event(String username, Action action, String details) {
        this.username = username;
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + username + " - " + action + ": " + details;
    }
}
