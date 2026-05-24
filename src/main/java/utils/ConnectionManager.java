package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/todo_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "12345";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Драйвер PostgreSQL не найден!");
            System.err.println("Добавьте зависимость в pom.xml");
        }
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к БД: " + e.getMessage());
            System.err.println("Проверьте:");
            System.err.println("  1. Запущен ли PostgreSQL");
            System.err.println("  2. Правильный ли пароль в ConnectionManager");
            System.err.println("  3. Существует ли база данных 'todo_db'");
            return null;
        }
    }

    private ConnectionManager() {
    }
}