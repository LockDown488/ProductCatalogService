package ru.kopanev.repository;

import ru.kopanev.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    void save(Event event);

    Optional<Event> findById(Long id);

    List<Event> findAll();

    List<Event> findByUsername(String username);

}
