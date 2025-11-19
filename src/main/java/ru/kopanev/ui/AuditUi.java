package ru.kopanev.ui;

import lombok.RequiredArgsConstructor;
import ru.kopanev.model.Event;
import ru.kopanev.service.AuditService;

import java.util.List;

/**
 * UI-компонент для отображения событий аудита.
 * Предоставляет методы для вывода истории действий всех пользователей
 * или конкретного пользователя в консоль.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@RequiredArgsConstructor
public class AuditUi {
    private final AuditService auditService;

    /**
     * Выводит все события аудита в консоль.
     * Если событий нет, выводит соответствующее сообщение.
     */
    public void printAllEvents() {
        List<Event> events = auditService.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("Нет событий аудита.");
            return;
        }

        System.out.println("\n=== ИСТОРИЯ ДЕЙСТВИЙ ===");
        events.forEach(System.out::println);
    }

    /**
     * Выводит события аудита для конкретного пользователя.
     * Если событий нет, выводит соответствующее сообщение.
     *
     * @param username имя пользователя, для которого нужно вывести историю
     */
    public void printUserEvents(String username) {
        List<Event> events = auditService.getEventsByUsername(username);

        if (events.isEmpty()) {
            System.out.println("Нет событий для пользователя: " + username);
            return;
        }

        System.out.println("\n=== ИСТОРИЯ ДЕЙСТВИЙ: " + username + " ===");
        events.forEach(System.out::println);
    }
}