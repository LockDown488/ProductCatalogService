package ru.kopanev.service;

import ru.kopanev.enums.Action;
import ru.kopanev.model.Event;

import java.util.ArrayList;
import java.util.List;

public class AuditService {

    private final List<Event> events = new ArrayList<>();

    public void log(String username, Action action, String details) {
        Event event = new Event(username, action, details);
        events.add(event);
        System.out.println(event);
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }
}