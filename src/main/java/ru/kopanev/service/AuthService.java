package ru.kopanev.service;

/**
 * Сервис для управления аутентификацией и авторизацией пользователей.
 * Обеспечивает регистрацию, вход и выход пользователей из системы.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
public interface AuthService {

    /**
     * Регистрирует нового пользователя в системе.
     * Проверяет уникальность имени пользователя.
     *
     * @param username имя пользователя (не должно быть null или пустым)
     * @param password пароль пользователя (не должен быть null или пустым)
     * @return true, если регистрация успешна; false, если пользователь уже существует
     * @throws IllegalArgumentException если username или password пусты
     */
    boolean register(String username, String password);

    /**
     * Выполняет вход пользователя в систему.
     * Проверяет корректность имени пользователя и пароля.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return true, если вход выполнен успешно; false, если данные неверны
     * @throws IllegalArgumentException если username или password пусты
     */
    boolean login(String username, String password);

    /**
     * Выполняет выход текущего пользователя из системы.
     * Обновляет статус пользователя на неактивный.
     */
    void logout();

    /**
     * Проверяет, авторизован ли пользователь в данный момент.
     *
     * @return true, если пользователь авторизован; false в противном случае
     */
    boolean isLoggedIn();
}
