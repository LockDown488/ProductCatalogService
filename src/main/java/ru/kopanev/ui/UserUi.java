package ru.kopanev.ui;

import lombok.RequiredArgsConstructor;
import ru.kopanev.enums.Action;
import ru.kopanev.service.AuditService;
import ru.kopanev.service.AuthService;
import ru.kopanev.utils.UserSession;

import java.util.Scanner;

/**
 * UI-компонент для работы с аутентификацией пользователей.
 * Предоставляет методы для регистрации, входа и выхода пользователей
 * через консольный интерфейс.
 *
 * <p>Все операции автоматически логируются в систему аудита.</p>
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@RequiredArgsConstructor
public class UserUi {
    private final Scanner scanner;
    private final UserSession session;
    private final AuthService authService;
    private final AuditService auditService;

    /**
     * Регистрирует нового пользователя в системе.
     * Запрашивает имя пользователя и пароль, затем создаёт учётную запись.
     */
    public void register() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine().trim();

        if (authService.register(username, password)) {
            System.out.println("Регистрация успешна");
        } else {
            System.out.println("Пользователь с таким именем уже существует");
        }
    }

    /**
     * Выполняет вход пользователя в систему.
     * Запрашивает имя пользователя и пароль, затем аутентифицирует пользователя.
     * При успешном входе создаёт сессию пользователя.
     */
    public void login() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine().trim();

        if (authService.login(username, password)) {
            session.login(username);
            System.out.println("Вход выполнен");
        } else {
            System.out.println("Неверное имя пользователя или пароль");
        }
    }

    /**
     * Выполняет выход текущего пользователя из системы.
     * Логирует действие в аудит и завершает сессию пользователя.
     */
    public void logout() {
        System.out.println("Пользователь " + session.getCurrentUser() + " вышел");
        auditService.logAction(session.getCurrentUser(), Action.LOGOUT, "Пользователь вышел");
        session.logout();
    }
}
