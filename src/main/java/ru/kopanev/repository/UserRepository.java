package ru.kopanev.repository;

import ru.kopanev.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Выполняет CRUD операции и поиск пользователей в БД.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
public interface UserRepository {

    /**
     * Сохраняет нового пользователя.
     * @param user пользователь для сохранения
     */
    void save(User user);

    /**
     * Обновляет данные пользователя.
     * @param user пользователь с обновлёнными данными
     */
    void update(User user);

    /**
     * Находит пользователя по ID.
     * @param id идентификатор пользователя
     * @return Optional с пользователем или пустой
     */
    Optional<User> findById(Long id);

    /**
     * Находит пользователя по имени.
     * @param username имя пользователя
     * @return Optional с пользователем или пустой
     */
    Optional<User> findByUsername(String username);

    /**
     * Возвращает всех пользователей.
     * @return список всех пользователей
     */
    List<User> findAll();
}
