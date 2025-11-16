package ru.kopanev;

import lombok.extern.slf4j.Slf4j;
import ru.kopanev.config.DbConfig;
import ru.kopanev.factory.DataSourceFactory;
import ru.kopanev.ui.MenuUi;
import ru.kopanev.factory.ApplicationFactory;
import ru.kopanev.utils.LiquibaseRunner;

import javax.sql.DataSource;

@Slf4j
public class Main {
    public static void main(String[] args) {
        try {
            log.info("Start Marketplace application...");

            DbConfig dbConfig = new DbConfig();
            DataSource dataSource = DataSourceFactory.getDataSource();

            LiquibaseRunner liquibaseRunner = new LiquibaseRunner(dbConfig, dataSource);
            liquibaseRunner.runMigrations();

            ApplicationFactory factory = new ApplicationFactory();
            MenuUi ui = factory.createApplication();
            ui.start();
        } catch (Exception e) {
            log.error("Failed to start application", e);
            System.exit(1);
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                DataSourceFactory.close();
                log.info("Application stopped");
            }));
        }
    }
}