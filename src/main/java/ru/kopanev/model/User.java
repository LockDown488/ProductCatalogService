package ru.kopanev.model;

import lombok.*;

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