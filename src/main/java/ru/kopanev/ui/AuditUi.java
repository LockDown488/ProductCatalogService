package ru.kopanev.ui;

import lombok.RequiredArgsConstructor;
import ru.kopanev.model.Event;
import ru.kopanev.service.AuditService;

import java.util.List;

@RequiredArgsConstructor
public class AuditUi {
    private final AuditService auditService;

    public void printAllEvents() {
        List<Event> events = auditService.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("Нет событий аудита.");
            return;
        }

        System.out.println("\n=== ИСТОРИЯ ДЕЙСТВИЙ ===");
        events.forEach(System.out::println);
    }

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
