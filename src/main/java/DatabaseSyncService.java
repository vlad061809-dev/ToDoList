import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class DatabaseSyncService {
    private final TaskDatabaseDAO dbDAO = new TaskDatabaseDAO();

    public void saveToDatabase(Task task) {
        try {
            dbDAO.save(task);
            log.info("Задача синхронизирована с БД");
        } catch (Exception e) {
            log.error("Ошибка синхронизации с БД: {}", e.getMessage());
        }
    }

    public List<Task> saveAllToDatabase(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            log.warn("Попытка синхронизировать пустой список задач");
            return tasks;
        }

        String sql = "INSERT INTO tasks (id, title, description, deadline, priority, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);  // важный шаг!

            int batchSize = 0;
            for (Task task : tasks) {
                ps.setInt(1, task.getId());
                ps.setString(2, task.getTitle());
                ps.setString(3, task.getDescription());
                ps.setObject(4, task.getDeadline());
                ps.setInt(5, task.getPriority());
                ps.setString(6, task.getStatus().name());
                ps.addBatch();
                batchSize++;

                if (batchSize % 1000 == 0) {
                    ps.executeBatch();
                    batchSize = 0;
                }
            }
            if (batchSize > 0) {
                ps.executeBatch();
            }

            conn.commit();
            log.info("Все задачи синхронизированы с БД (batch, {} записей)", tasks.size());

        } catch (SQLException e) {
            log.error("Ошибка batch синхронизации: {}", e.getMessage());
        }
        return tasks;
    }

        public List<Task> loadFromDatabase () {
            List<Task> loadedTasks = new ArrayList<>();
            try {
                loadedTasks = dbDAO.findAll();
                log.info("Загружено {} задач из БД", loadedTasks.size());
            } catch (Exception error) {
                log.error("Ошибка при загрузке данных: {}", error.getMessage(), error);
            }
            return loadedTasks;
        }
    }

