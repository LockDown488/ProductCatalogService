package ru.kopanev.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.enums.Action;
import ru.kopanev.model.Event;
import ru.kopanev.factory.DataSourceFactory;
import ru.kopanev.repository.EventRepository;
import ru.kopanev.utils.SqlQueries;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class EventRepositoryImpl implements EventRepository {
    private final DataSource dataSource;

    public EventRepositoryImpl() {
        this.dataSource = DataSourceFactory.getDataSource();
    }

    @Override
    public void save(Event event) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.SAVE_EVENT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, event.getUsername());
            stmt.setString(2, event.getAction().name());
            stmt.setString(3, event.getDetails());
            stmt.setTimestamp(4, Timestamp.valueOf(event.getTimestamp()));

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    event.setId(keys.getLong(1));
                }
            }
            log.debug("Audit event saved: action={}, user={}", event.getAction(), event.getUsername());
        } catch (SQLException e) {
            log.error("Failed to save audit event", e);
            throw new RuntimeException("Failed to save audit event", e);
        }
    }

    @Override
    public Optional<Event> findById(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_EVENT_BY_ID, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToEvent(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find event with ID {}", id, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_ALL_EVENTS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) events.add(mapRowToEvent(rs));
        } catch (SQLException e) {
            log.error("Failed to find all audit events", e);
        }
        return events;
    }

    @Override
    public List<Event> findByUsername(String username) {
        List<Event> events = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_EVENTS_BY_USERNAME)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) events.add(mapRowToEvent(rs));
            }
        } catch (SQLException e) {
            log.error("Failed to find audit events for user: {}", username, e);
        }
        return events;
    }

    private Event mapRowToEvent(ResultSet rs) throws SQLException {
        return new Event(
                rs.getLong("id"),
                rs.getString("username"),
                Action.valueOf(rs.getString("action")),
                rs.getString("details"),
                rs.getTimestamp("timestamp").toLocalDateTime()
        );
    }
}
