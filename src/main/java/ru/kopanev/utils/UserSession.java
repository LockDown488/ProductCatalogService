package ru.kopanev.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSession {
    private String currentUser;

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
