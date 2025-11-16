package ru.kopanev.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class UserSession {
    private String currentUser;
    private boolean isAuthenticated;

    public void login(String username) {
        this.currentUser = username;
        this.isAuthenticated = true;
        log.info("Session started for user: {}", username);
    }

    public void logout() {
        log.info("Session ended for user: {}", currentUser);
        this.currentUser = null;
        this.isAuthenticated = false;
    }
}
