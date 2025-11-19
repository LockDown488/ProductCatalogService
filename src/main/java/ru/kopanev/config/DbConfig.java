package ru.kopanev.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Конфигурация подключения к базе данных.
 * Загружает настройки из файла application.properties и предоставляет
 * доступ к параметрам БД, пула соединений и Liquibase.
 *
 * <p>При отсутствии или некорректных значениях свойств используются значения по умолчанию.</p>
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@Slf4j
public class DbConfig {
    private static final String CONFIG_FILE = "application.properties";
    private final Properties properties;

    /**
     * Создаёт новый экземпляр конфигурации и загружает свойства из файла.
     *
     * @throws RuntimeException если файл не найден или не может быть загружен
     */
    public DbConfig() {
        this.properties = loadProperties();
    }

    /**
     * Загружает свойства из файла конфигурации.
     *
     * @return объект Properties с загруженными свойствами
     * @throws RuntimeException если файл не найден или произошла ошибка чтения
     */
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
        return props;
    }

    /**
     * Возвращает URL подключения к базе данных.
     * @return JDBC URL
     */
    public String getDatabaseUrl() {
        return properties.getProperty("database.url");
    }

    /**
     * Возвращает имя пользователя для подключения к БД.
     * @return имя пользователя
     */
    public String getDatabaseUsername() {
        return properties.getProperty("database.username");
    }

    /**
     * Возвращает пароль для подключения к БД.
     * @return пароль
     */
    public String getDatabasePassword() {
        return properties.getProperty("database.password");
    }

    /**
     * Возвращает класс JDBC драйвера.
     * @return полное имя класса драйвера
     */
    public String getDatabaseDriver() {
        return properties.getProperty("database.driver");
    }

    /**
     * Возвращает максимальный размер пула соединений.
     * @return максимальное количество соединений (по умолчанию 10)
     */
    public int getMaximumPoolSize() {
        return parseIntProperty("database.pool.maximum-pool-size", 10);
    }

    /**
     * Возвращает минимальное количество неактивных соединений.
     * @return минимальное количество idle соединений (по умолчанию 2)
     */
    public int getMinimumIdle() {
        return parseIntProperty("database.pool.minimum-idle", 2);
    }

    /**
     * Возвращает таймаут получения соединения из пула.
     * @return таймаут в миллисекундах (по умолчанию 30000)
     */
    public long getConnectionTimeout() {
        return parseLongProperty("database.pool.connection-timeout", 30000);
    }

    /**
     * Проверяет, включено ли кэширование prepared statements.
     * @return true, если кэширование включено (по умолчанию true)
     */
    public boolean isCachePrepStmts() {
        return parseBooleanProperty("database.cache.prep-stmts", true);
    }

    /**
     * Возвращает размер кэша prepared statements.
     * @return размер кэша (по умолчанию 250)
     */
    public int getPrepStmtCacheSize() {
        return parseIntProperty("database.cache.prep-stmt-cache-size", 250);
    }

    /**
     * Возвращает максимальную длину SQL для кэширования.
     * @return максимальная длина в символах (по умолчанию 2048)
     */
    public int getPrepStmtCacheSqlLimit() {
        return parseIntProperty("database.cache.prep-stmt-cache-sql-limit", 2048);
    }

    /**
     * Возвращает путь к главному changelog файлу Liquibase.
     * @return путь к файлу changelog
     */
    public String getLiquibaseChangelog() {
        return properties.getProperty("liquibase.changelog");
    }

    /**
     * Возвращает контексты Liquibase для выполнения.
     * @return контексты (например, "dev", "prod")
     */
    public String getLiquibaseContexts() {
        return properties.getProperty("liquibase.contexts");
    }

    /**
     * Возвращает схему БД по умолчанию для миграций.
     * @return имя схемы
     */
    public String getLiquibaseDefaultSchema() {
        return properties.getProperty("liquibase.default-schema");
    }

    /**
     * Возвращает схему для служебных таблиц Liquibase.
     * @return имя схемы для таблиц Liquibase
     */
    public String getLiquibaseSchema() {
        return properties.getProperty("liquibase.liquibase-schema");
    }

    /**
     * Парсит целочисленное свойство с обработкой исключений.
     *
     * @param key ключ свойства
     * @param defaultValue значение по умолчанию
     * @return значение из properties или defaultValue при ошибке
     */
    private int parseIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            log.warn("Property '{}' is not set, using default value: {}", key, defaultValue);
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            log.error("Invalid integer value for property '{}': '{}', using default value: {}",
                    key, value, defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Парсит long свойство с обработкой исключений.
     *
     * @param key ключ свойства
     * @param defaultValue значение по умолчанию
     * @return значение из properties или defaultValue при ошибке
     */
    private long parseLongProperty(String key, long defaultValue) {
        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            log.warn("Property '{}' is not set, using default value: {}", key, defaultValue);
            return defaultValue;
        }

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            log.error("Invalid long value for property '{}': '{}', using default value: {}",
                    key, value, defaultValue, e);
            return defaultValue;
        }
    }

    /**
     * Парсит boolean свойство с обработкой исключений.
     *
     * @param key ключ свойства
     * @param defaultValue значение по умолчанию
     * @return значение из properties или defaultValue при ошибке
     */
    private boolean parseBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            log.warn("Property '{}' is not set, using default value: {}", key, defaultValue);
            return defaultValue;
        }

        try {
            return Boolean.parseBoolean(value.trim());
        } catch (Exception e) {
            log.error("Invalid boolean value for property '{}': '{}', using default value: {}",
                    key, value, defaultValue, e);
            return defaultValue;
        }
    }
}
