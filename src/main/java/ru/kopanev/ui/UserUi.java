package ru.kopanev.ui;

import lombok.RequiredArgsConstructor;
import ru.kopanev.enums.Action;
import ru.kopanev.service.AuditService;
import ru.kopanev.service.AuthService;
import ru.kopanev.utils.UserSession;

import java.util.Scanner;

@RequiredArgsConstructor
public class UserUi {
    private final Scanner scanner;
    private final UserSession session;

    private final AuthService authService;
    private final AuditService auditService;

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

    public void logout() {
        System.out.println("Пользователь " + session.getCurrentUser() + " вышел");
        auditService.logAction(session.getCurrentUser(), Action.LOGOUT, "Пользователь вышел");
        session.logout();
    }
}
