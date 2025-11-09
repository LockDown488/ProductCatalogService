package ru.kopanev.model;

import ru.kopanev.enums.Action;

import java.time.LocalDateTime;

public class Event {

    private final String username;
    private final Action action;
    private final String details;
    private final LocalDateTime timestamp;

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
