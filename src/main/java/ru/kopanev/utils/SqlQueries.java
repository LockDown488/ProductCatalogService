package ru.kopanev.utils;

/**
 * Константы SQL запросов для работы с базой данных.
 * Все запросы централизованы для упрощения поддержки и изменений.
 *
 * @author Artem Kopanev
 * @since 1.0
 */
public final class SqlQueries {
    private SqlQueries() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Event Repository queries
    public static final String SAVE_EVENT =
            "INSERT INTO marketplace.audit_events (username, action, details, timestamp) VALUES (?, ?, ?, ?)";

    public static final String FIND_EVENT_BY_ID =
            "SELECT id, username, action, details, timestamp FROM marketplace.audit_events WHERE id = ?";

    public static final String FIND_ALL_EVENTS =
            "SELECT id, username, action, details, timestamp FROM marketplace.audit_events ORDER BY timestamp DESC";

    public static final String FIND_EVENTS_BY_USERNAME =
            "SELECT id, username, action, details, timestamp FROM marketplace.audit_events WHERE username=? ORDER BY timestamp DESC";

    // Product Repository queries
    public static final String SAVE_PRODUCT =
            "INSERT INTO marketplace.products (name, category, brand, price, description) VALUES (?, ?, ?, ?, ?)";

    public static final String UPDATE_PRODUCT =
            "UPDATE marketplace.products SET name=?, category=?, brand=?, price=?, description=? WHERE id=?";

    public static final String DELETE_PRODUCT =
            "DELETE FROM marketplace.products WHERE id = ?";

    public static final String FIND_PRODUCT_BY_ID =
            "SELECT id, name, category, brand, price, description FROM marketplace.products WHERE id=?";

    public static final String FIND_ALL_PRODUCTS =
            "SELECT id, name, category, brand, price, description FROM marketplace.products ORDER BY id";

    public static final String FIND_PRODUCT_BY_CATEGORY =
            "SELECT id, name, category, brand, price, description FROM marketplace.products WHERE category = ? ORDER BY name";

    public static final String FIND_PRODUCT_BY_BRAND =
            "SELECT id, name, category, brand, price, description FROM marketplace.products WHERE brand = ? ORDER BY name";

    public static final String FIND_PRODUCT_BY_PRICE_RANGE =
            "SELECT id, name, category, brand, price, description FROM marketplace.products WHERE price BETWEEN ? AND ? ORDER BY price";

    // User Repository queries
    public static final String SAVE_USER =
            "INSERT INTO marketplace.users (username, password, is_active) VALUES (?, ?, ?)";

    public static final String UPDATE_USER =
            "UPDATE marketplace.users SET password=?, is_active=? WHERE username=?";

    public static final String FIND_USER_BY_ID =
            "SELECT id, username, password, is_active FROM marketplace.users WHERE id = ?";

    public static final String FIND_USER_BY_USERNAME =
            "SELECT id, username, password, is_active FROM marketplace.users WHERE username = ?";

    public static final String FIND_ALL_USERS =
            "SELECT id, username, password, is_active FROM marketplace.users";
}
