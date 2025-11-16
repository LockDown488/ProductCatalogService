package ru.kopanev.repository;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.model.User;
import ru.kopanev.factory.DataSourceFactory;

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
        String sql = "INSERT INTO marketplace.users (username, password, is_active) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
        String sql = "UPDATE marketplace.users SET password=?, is_active=? WHERE username=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "SELECT id, username, password, is_active FROM marketplace.users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "SELECT id, username, password, is_active FROM marketplace.users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "SELECT id, username, password, is_active FROM marketplace.users";
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
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
