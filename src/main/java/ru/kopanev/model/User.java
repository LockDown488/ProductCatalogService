package ru.kopanev.model;

import lombok.*;

/**
 * Представляет пользователя системы.
 * Содержит учётные данные и статус активности пользователя.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String username;
    private String password;
    private boolean isActive;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isActive = false;
    }
}