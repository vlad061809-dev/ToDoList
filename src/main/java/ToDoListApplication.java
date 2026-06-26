import lombok.extern.slf4j.Slf4j;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class ToDoListApplication {

    public static void main(String[] args) {
        log.info("=== Запуск TodoList Application ===");

        createTableIfNotExists();

        TaskManager taskManager = new TaskManager();
        taskManager.start();

        log.info("=== Приложение завершило работу ===");
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
            log.info("Таблица tasks проверена/создана");
        } catch (SQLException e) {
            log.error("Ошибка создания таблицы: {}", e.getMessage(), e);
        }
    }
}