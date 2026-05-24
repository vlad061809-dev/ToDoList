import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ToDoListApplication {

    public static void main(String[] args) {
        createTableIfNotExists();

        TaskManager taskManager = new TaskManager();
        taskManager.start();
    }

    private static void testConnection() {
        try (Connection conn = ConnectionManager.open()) {
            if (conn != null) {
                System.out.println("Подключение к БД успешно!");
            } else {
                System.out.println("Программа будет работать без сохранения в БД");
            }
        } catch (Exception e) {
            System.out.println("Программа будет работать без сохранения в БД");
        }
    }

    private static void createTableIfNotExists() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                description TEXT,
                deadline DATE NOT NULL,
                priority INTEGER DEFAULT 0,
                status VARCHAR(20) DEFAULT 'NEW'
            )
            """;

        try (Connection conn = ConnectionManager.open();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.err.println("Ошибка создания таблицы: " + e.getMessage());
        }
    }
}