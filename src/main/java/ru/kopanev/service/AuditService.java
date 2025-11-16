package ru.kopanev.service;

import ru.kopanev.enums.Action;
import ru.kopanev.model.Event;

import java.util.List;

public interface AuditService {

    void logAction(String username, Action action, String details);

    List<Event> getAllEvents();

    List<Event> getEventsByUsername(String username);
}
