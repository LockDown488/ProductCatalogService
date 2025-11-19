package ru.kopanev.factory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import ru.kopanev.config.DbConfig;

import javax.sql.DataSource;

/**
 * Фабрика для создания и управления пулом соединений с базой данных.
 * Использует HikariCP для эффективного управления соединениями.
 *
 * <p>Реализует паттерн Singleton — DataSource создаётся один раз
 * и переиспользуется во всём приложении.</p>
 *
 * <p>Настройки загружаются из {@link DbConfig}, включая:
 * параметры подключения, размер пула, кэширование PreparedStatements.</p>
 *
 * @author Artem Kopanev
 * @since 1.0
 */
@Slf4j
public class DataSourceFactory {

    private static DataSource dataSource;

    /**
     * Возвращает экземпляр DataSource (Singleton).
     * При первом вызове создаёт и настраивает HikariCP пул соединений.
     * Последующие вызовы возвращают уже созданный экземпляр.
     *
     * @return настроенный DataSource с пулом соединений HikariCP
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            DbConfig config = new DbConfig();

            HikariConfig hikariConfig = getHikariConfig(config);

            if (config.isCachePrepStmts()) {
                addCacheProperties(hikariConfig, config);
            }

            dataSource = new HikariDataSource(hikariConfig);

            log.info("HikariCP DataSource initialized");
            log.debug("Database URL: {}", config.getDatabaseUrl());
            log.debug("Maximum pool size: {}", config.getMaximumPoolSize());
            log.debug("Minimum idle connections: {}", config.getMinimumIdle());
            log.debug("PreparedStatement cache enabled: {}", config.isCachePrepStmts());
        }

        return dataSource;
    }

    /**
     * Создаёт и настраивает конфигурацию HikariCP.
     * Устанавливает параметры подключения и настройки пула.
     *
     * @param config конфигурация базы данных
     * @return настроенный объект HikariConfig
     */
    private static HikariConfig getHikariConfig(DbConfig config) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(config.getDatabaseUrl());
        hikariConfig.setUsername(config.getDatabaseUsername());
        hikariConfig.setPassword(config.getDatabasePassword());
        hikariConfig.setDriverClassName(config.getDatabaseDriver());

        hikariConfig.setMaximumPoolSize(config.getMaximumPoolSize());
        hikariConfig.setMinimumIdle(config.getMinimumIdle());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout());

        return hikariConfig;
    }

    /**
     * Добавляет свойства для кэширования PreparedStatements.
     * Улучшает производительность за счёт переиспользования подготовленных запросов.
     *
     * @param hikariConfig конфигурация HikariCP для модификации
     * @param config конфигурация с параметрами кэша
     */
    private static void addCacheProperties(HikariConfig hikariConfig, DbConfig config) {
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", String.valueOf(config.getPrepStmtCacheSize()));
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", String.valueOf(config.getPrepStmtCacheSqlLimit()));

        log.debug("PreparedStatement cache configured: size={}, sqlLimit={}",
                config.getPrepStmtCacheSize(),
                config.getPrepStmtCacheSqlLimit());
    }

    /**
     * Закрывает пул соединений HikariCP.
     * Освобождает все ресурсы, связанные с подключением к БД.
     * Вызывается при завершении работы приложения.
     */
    public static void close() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
            log.info("HikariCP DataSource closed");
        }
    }
}
