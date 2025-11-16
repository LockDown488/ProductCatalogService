package ru.kopanev.utils;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.kopanev.config.DbConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@RequiredArgsConstructor
public class LiquibaseRunner {

    private final DbConfig config;
    private final DataSource dataSource;

    public void runMigrations() {
        log.info("Start Liquibase migrations...");

        try (Connection conn = dataSource.getConnection()) {
            createSchemasIfNotExist(conn);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));

            database.setLiquibaseSchemaName(config.getLiquibaseSchema());
            database.setDefaultSchemaName(config.getLiquibaseDefaultSchema());

            Liquibase liquibase = new Liquibase(
                    config.getLiquibaseChangelog(),
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update(config.getLiquibaseContexts());

            log.info("Liquibase migrations completed successfully");
        } catch (SQLException | LiquibaseException e) {
            log.error("Failed to run Liquibase migrations", e);
            throw new RuntimeException("Database migration failed", e);
        }
    }

    public void rollbackLastChange() {
        log.warn("Rolling back last Liquibase changeset...");

        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            database.setLiquibaseSchemaName(config.getLiquibaseSchema());
            database.setDefaultSchemaName(config.getLiquibaseDefaultSchema());

            Liquibase liquibase = new Liquibase(
                    config.getLiquibaseChangelog(),
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.rollback(1, config.getLiquibaseContexts());

            log.info("Rollback completed successfully");

        } catch (Exception e) {
            log.error("Failed to rollback Liquibase migration", e);
            throw new RuntimeException("Database rollback failed", e);
        }
    }

    private void createSchemasIfNotExist(Connection connection) throws SQLException {
        log.info("Creating schemas if not exist...");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE SCHEMA IF NOT EXISTS marketplace");
            log.debug("Schema 'marketplace' ensured");

            stmt.execute("CREATE SCHEMA IF NOT EXISTS liquibase_service");
            log.debug("Schema 'liquibase_service' ensured");
        }

        log.info("Schemas created successfully");
    }
}
