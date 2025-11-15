package ru.kopanev.service;

import ru.kopanev.enums.Action;
import ru.kopanev.model.User;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private final Map<String, User> users = new HashMap<>();
    private final AuditService auditService;

    public AuthService(AuditService auditService) {
        this.auditService = auditService;
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }

        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        users.put(username, user);
        auditService.log(username, Action.REGISTER, "Пользователь " + username + " зарегистрирован!");

        return true;
    }

    public boolean login(String username, String password) {
        if (!users.containsKey(username)) {
            return false;
        }

        if (users.get(username).getPassword().equals(password)) {
            auditService.log(username, Action.LOGIN, "Пользователь " + username + " авторизован!");
            return true;
        }

        return false;
    }
}