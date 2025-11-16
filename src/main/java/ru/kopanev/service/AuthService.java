package ru.kopanev.service;

public interface AuthService {

    boolean register(String username, String password);

    boolean login(String username, String password);

    void logout();

    boolean isLoggedIn();
}
