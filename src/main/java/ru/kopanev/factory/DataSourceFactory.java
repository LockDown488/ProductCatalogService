package ru.kopanev.factory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import ru.kopanev.config.DbConfig;

import javax.sql.DataSource;

@Slf4j
public class DataSourceFactory {

    private static DataSource dataSource;

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

    private static void addCacheProperties(HikariConfig hikariConfig, DbConfig config) {
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", String.valueOf(config.getPrepStmtCacheSize()));
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", String.valueOf(config.getPrepStmtCacheSqlLimit()));

        log.debug("PreparedStatement cache configured: size={}, sqlLimit={}",
                config.getPrepStmtCacheSize(),
                config.getPrepStmtCacheSqlLimit());
    }

    public static void close() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
            log.info("HikariCP DataSource closed");
        }
    }
}
