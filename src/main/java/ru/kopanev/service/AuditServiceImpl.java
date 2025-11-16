package ru.kopanev.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kopanev.enums.Action;
import ru.kopanev.model.Event;
import ru.kopanev.repository.EventRepository;
import ru.kopanev.utils.UserSession;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final EventRepository eventRepository;

    public void logAction(String username, Action action, String details) {
        Event event = new Event(username, action, details);
        event.setTimestamp(LocalDateTime.now());
        eventRepository.save(event);
        log.debug("Audit event logged: user={}, action={}, details={}", username, action, details);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsByUsername(String username) {
        return eventRepository.findByUsername(username);
    }
}