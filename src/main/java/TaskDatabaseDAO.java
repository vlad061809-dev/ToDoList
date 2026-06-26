import lombok.extern.slf4j.Slf4j;
import utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j


public class TaskDatabaseDAO implements TaskDAO {

    private Throwable e;

    @Override
    public void save(Task task) {
        String insertQuery = "INSERT INTO tasks (id, title, description, deadline, priority, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection dbConnection = ConnectionManager.open();
             PreparedStatement preparedStmt = dbConnection.prepareStatement(insertQuery)) {

            preparedStmt.setInt(1, task.getId());
            preparedStmt.setString(2, task.getTitle());
            preparedStmt.setString(3, task.getDescription());
            preparedStmt.setObject(4, task.getDeadline());
            preparedStmt.setInt(5, task.getPriority());
            preparedStmt.setString(6, task.getStatus().name());

            preparedStmt.executeUpdate();

        } catch (SQLException dbError) {
            log.error("Ошибка сохранения задачи: {}", dbError.getMessage());
            throw new RuntimeException("Не удалось сохранить задачу", dbError);
        }
    }

    @Override
    public List<Task> findAll() {
        return List.of();
    }


    public void saveAll(List<Task> tasks) {
        String insertQuery = "INSERT INTO tasks (id, title, description, deadline, priority, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection dbConnection = ConnectionManager.open();
             PreparedStatement preparedStmt = dbConnection.prepareStatement(insertQuery)) {

            dbConnection.setAutoCommit(false);

            int batchSize = 0;

            for (Task task : tasks) {
                preparedStmt.setInt(1, task.getId());
                preparedStmt.setString(2, task.getTitle());
                preparedStmt.setString(3, task.getDescription());
                preparedStmt.setObject(4, task.getDeadline());
                preparedStmt.setInt(5, task.getPriority());
                preparedStmt.setString(6, task.getStatus().name());

                preparedStmt.addBatch();  // Добавляем в batch
                batchSize++;

                if (batchSize % 1000 == 0) {
                    preparedStmt.executeBatch();
                    batchSize = 0;
                    log.info("Сохранено {} задач...", tasks.size());
                }
            }

            if (batchSize > 0) {
                preparedStmt.executeBatch();
            }

            dbConnection.commit();
            log.info("Успешно сохранено {} задач в БД", tasks.size());

        } catch (SQLException dbError) {
            log.error("Ошибка batch сохранения задач: {}", dbError.getMessage());
            throw new RuntimeException("Не удалось сохранить задачи", dbError);
        }
    }

    @Override
    public Optional<Task> findById(int taskId) {
        String selectQuery = "SELECT * FROM tasks WHERE id = ?";

        try (Connection dbConnection = ConnectionManager.open();
             PreparedStatement preparedStmt = dbConnection.prepareStatement(selectQuery)) {

            preparedStmt.setInt(1, taskId);
            ResultSet queryResult = preparedStmt.executeQuery();

            if (queryResult.next()) {
                Task foundTask = mapResultSetToTask(queryResult);
                return Optional.of(foundTask);
            }

        } catch (SQLException dbError) {
            log.error("Ошибка поиска задачи: {}", dbError.getMessage());
            throw new RuntimeException("Не удалось найти задачу", dbError);
        }

        return Optional.empty();
    }

//    @Override
//    public List<Task> findAll() {
//        String selectAllQuery = "SELECT * FROM tasks ORDER BY id";
//        List<Task> tasksList = new ArrayList<>();
//
//        try (Connection dbConnection = ConnectionManager.open();
//             Statement sqlStatement = dbConnection.createStatement();
//             ResultSet queryResult = sqlStatement.executeQuery(selectAllQuery)) {
//
//            while (queryResult.next()) {
//                Task currentTask = mapResultSetToTask(queryResult);
//                tasksList.add(currentTask);
//            }
//
//        } catch (SQLException dbError) {
//            System.err.println("Ошибка загрузки задач: " + dbError.getMessage());
//        }
//
//        return tasksList;
//    }

    public List<Task> findTasksWithPagination(int minId, int page, int pageSize) {
        String sql = "SELECT id, title, description, deadline, priority, status" +
                "FROM tasks" +
                "WHERE id > ? " +
                "ORDER BY id " +
                "LIMIT ? OFFSET ?";

        List<Task> tasks = new ArrayList<>();

        try (Connection conn = ConnectionManager.open();
        PreparedStatement ps = conn.prepareStatement(sql)){

            int offset = page * pageSize;
            ps.setInt(1, minId);
            ps.setInt(2, pageSize);
            ps.setInt(3, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }

            log.info("Загружена страница {}, записей: {}", page, tasks.size());

        } catch(SQLException e){
            throw new RuntimeException("Ошибка пагинации", e);
        }

        return tasks;
    }

    public void loadAllWithPagination() {
        int pageSize = 1000;  
        int page = 0;
        int minId = 0;

        while (true) {
            List<Task> tasks = findTasksWithPagination(minId, page, pageSize);
            if (tasks.isEmpty()) break;

            processTasks(tasks);

            page++;
        }
    }

    private void processTasks(List<Task> tasks) {
    }


    @Override
public void update(Task task) {
    String updateQuery = "UPDATE tasks SET title = ?, description = ?, deadline = ?, priority = ?, status = ? WHERE id = ?";

    try (Connection dbConnection = ConnectionManager.open();
         PreparedStatement preparedStmt = dbConnection.prepareStatement(updateQuery)) {

        preparedStmt.setString(1, task.getTitle());
        preparedStmt.setString(2, task.getDescription());
        preparedStmt.setObject(3, task.getDeadline());
        preparedStmt.setInt(4, task.getPriority());
        preparedStmt.setString(5, task.getStatus().name());
        preparedStmt.setInt(6, task.getId());

        int updatedRows = preparedStmt.executeUpdate();
        if (updatedRows == 0) {
            log.info("Задача с ID {} не найдена", task.getId());
        }

    } catch (SQLException dbError) {
        log.error("Ошибка обновления задачи: {}", dbError.getMessage());
        throw new RuntimeException("Не удалось обновить задачу", dbError);
    }
}

@Override
public void delete(int taskId) {
    String deleteQuery = "DELETE FROM tasks WHERE id = ?";

    try (Connection dbConnection = ConnectionManager.open();
         PreparedStatement preparedStmt = dbConnection.prepareStatement(deleteQuery)) {

        preparedStmt.setInt(1, taskId);
        int deletedRows = preparedStmt.executeUpdate();

        if (deletedRows > 0) {
            log.info("Задача {} удалена из БД", taskId);
        } else {
            log.info("Задача с ID {} не найдена в БД", taskId);
        }

    } catch (SQLException dbError) {
        log.error("Ошибка удаления задачи: {}", dbError.getMessage());
        throw new RuntimeException("Не удалось удалить задачу", dbError);
    }
}

@Override
public void updateStatus(int taskId, Status newStatus) {
    String updateStatusQuery = "UPDATE tasks SET status = ? WHERE id = ?";

    try (Connection dbConnection = ConnectionManager.open();
         PreparedStatement preparedStmt = dbConnection.prepareStatement(updateStatusQuery)) {

        preparedStmt.setString(1, newStatus.name());
        preparedStmt.setInt(2, taskId);

        int updatedRows = preparedStmt.executeUpdate();
        if (updatedRows > 0) {
            log.info("Статус задачи {} обновлен на {}", taskId, newStatus);
        }

    } catch (SQLException dbError) {
        log.error("Ошибка обновления статуса: {}", dbError.getMessage());
        throw new RuntimeException("Не удалось обновить статус", dbError);
    }
}

private Task mapResultSetToTask(ResultSet resultSet) throws SQLException {
    return new Task(
            resultSet.getInt("id"),
            resultSet.getObject("deadline", LocalDate.class),
            resultSet.getString("title"),
            resultSet.getString("description"),
            resultSet.getInt("priority"),
            Status.valueOf(resultSet.getString("status")));
}
}

