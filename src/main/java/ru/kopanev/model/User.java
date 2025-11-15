package ru.kopanev.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class User {

    private final String username;
    private final String password;
    private final boolean isActive = false;

}