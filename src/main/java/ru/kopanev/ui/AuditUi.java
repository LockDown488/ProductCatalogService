package ru.kopanev.ui;

import lombok.RequiredArgsConstructor;
import ru.kopanev.model.Event;
import ru.kopanev.service.AuditService;

import java.util.List;

@RequiredArgsConstructor
public class AuditUi {
    private final AuditService auditService;

    public void viewAudit() {
        List<Event> events = auditService.getEvents();

        if (events.isEmpty()) {
            System.out.println("Журнал аудита пуст.");
            return;
        }

        System.out.println("\n=== ЖУРНАЛ АУДИТА ===");
        for (Event event : events) {
            System.out.println(event);
        }
    }
}
