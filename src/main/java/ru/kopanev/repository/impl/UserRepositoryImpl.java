package ru.kopanev.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.model.User;
import ru.kopanev.factory.DataSourceFactory;
import ru.kopanev.repository.UserRepository;
import ru.kopanev.utils.SqlQueries;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final DataSource dataSource;

    public UserRepositoryImpl() {
        this.dataSource = DataSourceFactory.getDataSource();
    }

    @Override
    public void save(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.SAVE_USER,Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setBoolean(3, user.isActive());

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
            }
            log.info("User saved: {}", user.getUsername());
        } catch (SQLException e) {
            log.error("Failed to save user: {}", user.getUsername(), e);
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public void update(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.UPDATE_USER)) {

            stmt.setString(1, user.getPassword());
            stmt.setBoolean(2, user.isActive());
            stmt.setString(3, user.getUsername());

            int rows = stmt.executeUpdate();
            if (rows == 0) throw new RuntimeException("User not found: " + user.getUsername());
            log.info("User updated: {}", user.getUsername());
        } catch (SQLException e) {
            log.error("Failed to update user: {}", user.getUsername(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_USER_BY_ID)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            log.error("Failed to find user with ID: {}", id, e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_USER_BY_USERNAME)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            log.error("Failed to find user: {}", username, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SqlQueries.FIND_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) users.add(mapRowToUser(rs));
        } catch (SQLException e) {
            log.error("Failed to find all users", e);
        }
        return users;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBoolean("is_active")
        );
    }
}
