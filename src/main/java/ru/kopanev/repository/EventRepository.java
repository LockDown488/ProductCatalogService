package ru.kopanev.repository;

import ru.kopanev.model.Event;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с событиями аудита.
 * Обеспечивает доступ к данным о действиях пользователей в системе.
 */
public interface EventRepository {

    /**
     * Сохраняет новое событие аудита в базу данных.
     * После сохранения событию присваивается уникальный идентификатор.
     *
     * @param event событие для сохранения (не должно быть null)
     * @throws IllegalArgumentException если event равен null
     */
    void save(Event event);

    /**
     * Находит событие по его уникальному идентификатору.
     *
     * @param id идентификатор события (должен быть положительным)
     * @return Optional с найденным событием или пустой Optional, если событие не найдено
     * @throws IllegalArgumentException если id меньше или равен нулю
     */
    Optional<Event> findById(Long id);

    /**
     * Возвращает все события аудита из базы данных.
     * События отсортированы по времени создания в порядке убывания (новые первыми).
     *
     * @return список всех событий, может быть пустым, но не null
     */
    List<Event> findAll();

    /**
     * Находит все события, связанные с конкретным пользователем.
     * События отсортированы по времени создания в порядке убывания.
     *
     * @param username имя пользователя для поиска (не должно быть null или пустым)
     * @return список событий пользователя, может быть пустым, но не null
     * @throws IllegalArgumentException если username равен null или пуст
     */
    List<Event> findByUsername(String username);
}
