package ru.kopanev.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Управляет сессией пользователя в приложении.
 * Хранит информацию о текущем пользователе и состоянии аутентификации.
 *
 * <p>Используется для отслеживания, какой пользователь в данный момент
 * работает с системой. Сессия создаётся при успешном входе и завершается
 * при выходе пользователя.</p>
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@Slf4j
@Getter
public class UserSession {

    private String currentUser;
    private boolean isAuthenticated;

    /**
     * Начинает сессию для пользователя.
     * Устанавливает текущего пользователя и флаг аутентификации.
     *
     * @param username имя пользователя, входящего в систему
     */
    public void login(String username) {
        this.currentUser = username;
        this.isAuthenticated = true;
        log.info("Session started for user: {}", username);
    }

    /**
     * Завершает текущую сессию пользователя.
     * Сбрасывает текущего пользователя и флаг аутентификации.
     */
    public void logout() {
        log.info("Session ended for user: {}", currentUser);
        this.currentUser = null;
        this.isAuthenticated = false;
    }
}