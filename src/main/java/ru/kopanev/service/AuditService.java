package ru.kopanev.service;

import ru.kopanev.enums.Action;
import ru.kopanev.model.Event;

import java.util.List;

/**
 * Сервис для управления аудитом действий пользователей.
 * Логирует все действия пользователей и предоставляет доступ к истории.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
public interface AuditService {

    /**
     * Записывает действие пользователя в систему аудита.
     *
     * @param username имя пользователя
     * @param action тип действия
     * @param details дополнительная информация (может быть null)
     * @throws IllegalArgumentException если username или action равны null
     */
    void logAction(String username, Action action, String details);

    /**
     * Возвращает все события аудита.
     * События отсортированы по времени (новые первыми).
     *
     * @return список всех событий
     */
    List<Event> getAllEvents();

    /**
     * Возвращает события аудита для указанного пользователя.
     * События отсортированы по времени (новые первыми).
     *
     * @param username имя пользователя
     * @return список событий пользователя
     * @throws IllegalArgumentException если username равен null
     */
    List<Event> getEventsByUsername(String username);
}
