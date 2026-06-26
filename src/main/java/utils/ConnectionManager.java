package utils;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public final class ConnectionManager {

    private static String url;
    private static String username;
    private static String password;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        String propertiesFile = "db/application.properties";
        Properties props = new Properties();

        try (InputStream input = ConnectionManager.class.getClassLoader()
                .getResourceAsStream(propertiesFile)) {

            if (input == null) {
                log.error("Файл {} не найден в classpath", propertiesFile);
                throw new RuntimeException("Файл " + propertiesFile + " не найден");
            }

            props.load(input);

            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");

            if (url == null || username == null || password == null) {
                log.error("Отсутствуют обязательные параметры в {}", propertiesFile);
                throw new RuntimeException("Отсутствуют параметры БД");
            }

            Class.forName("org.postgresql.Driver");
            log.info("PostgreSQL драйвер успешно загружен");
            log.debug("Подключение к БД: {}", url);

        } catch (ClassNotFoundException e) {
            log.error("Драйвер PostgreSQL не найден", e);
            throw new RuntimeException("Драйвер не найден", e);
        } catch (Exception e) {
            log.error("Ошибка загрузки конфигурации: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось загрузить настройки", e);
        }
    }

    public static Connection open() {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            log.debug("Успешное подключение к БД");
            return conn;
        } catch (SQLException e) {
            log.error("Ошибка подключения к БД: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка подключения", e);
        }
    }

    public static String getUrl() {
        return url;
    }

    private ConnectionManager() {
    }
}