package ru.kopanev.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kopanev.enums.Action;
import ru.kopanev.exception.EntityNotFoundException;
import ru.kopanev.model.User;
import ru.kopanev.repository.UserRepository;
import ru.kopanev.utils.UserSession;

@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuditService auditService;
    private final UserRepository userRepository;
    private final UserSession session;

    public boolean register(String username, String password) {
        log.info("Registration attempt: {}", username);

        if (userRepository.findByUsername(username).isPresent()) {
            log.warn("Registration failed: user already exists - {}", username);
            return false;
        }

        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        userRepository.save(user);
        auditService.logAction(username, Action.REGISTER, "Пользователь " + username + " зарегистрирован!");
        log.info("User registered successfully: {}", username);

        return true;
    }

    public boolean login(String username, String password) {
        log.info("Login attempt: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getPassword().equals(password)) {
            log.warn("Login failed: incorrect password - {}", username);
            return false;
        }

        user.setActive(true);
        userRepository.update(user);

        session.login(username);

        auditService.logAction(username, Action.LOGIN, "Пользователь " + username + " авторизован!");
        log.info("User logged in successfully: {}", username);
        return true;
    }

    public void logout() {
        String username = session.getCurrentUser();

        if (username == null) {
            log.warn("Logout attempt with no active session");
            return;
        }

        log.info("Logout: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setActive(false);
        userRepository.update(user);

        session.logout();

        auditService.logAction(username, Action.LOGOUT, "User logged out");
        log.info("User logged out: {}", username);
    }

    public boolean isLoggedIn() {
        return session.isAuthenticated();
    }
}