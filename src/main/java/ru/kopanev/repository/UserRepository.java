package ru.kopanev.repository;

import ru.kopanev.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    void update(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    List<User> findAll();
}
