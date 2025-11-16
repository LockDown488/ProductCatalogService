package ru.kopanev.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public class DbConfig {
    private static final String CONFIG_FILE = "application.properties";
    private final Properties properties;

    public DbConfig() {
        this.properties = loadProperties();
    }

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

    public String getDatabaseUrl() {
        return properties.getProperty("database.url");
    }

    public String getDatabaseUsername() {
        return properties.getProperty("database.username");
    }

    public String getDatabasePassword() {
        return properties.getProperty("database.password");
    }

    public String getDatabaseDriver() {
        return properties.getProperty("database.driver");
    }

    public int getMaximumPoolSize() {
        return Integer.parseInt(properties.getProperty("database.pool.maximum-pool-size", "10"));
    }

    public int getMinimumIdle() {
        return Integer.parseInt(properties.getProperty("database.pool.minimum-idle", "2"));
    }

    public long getConnectionTimeout() {
        return Long.parseLong(properties.getProperty("database.pool.connection-timeout", "30000"));
    }

    public boolean isCachePrepStmts() {
        return Boolean.parseBoolean(properties.getProperty("database.cache.prep-stmts", "true"));
    }

    public int getPrepStmtCacheSize() {
        return Integer.parseInt(properties.getProperty("database.cache.prep-stmt-cache-size", "250"));
    }

    public int getPrepStmtCacheSqlLimit() {
        return Integer.parseInt(properties.getProperty("database.cache.prep-stmt-cache-sql-limit", "2048"));
    }

    public String getLiquibaseChangelog() {
        return properties.getProperty("liquibase.changelog");
    }

    public String getLiquibaseContexts() {
        return properties.getProperty("liquibase.contexts");
    }

    public String getLiquibaseDefaultSchema() {
        return properties.getProperty("liquibase.default-schema");
    }

    public String getLiquibaseSchema() {
        return properties.getProperty("liquibase.liquibase-schema");
    }
}
