import utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDatabaseDAO implements TaskDAO {

    // 1. СОХРАНИТЬ задачу
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
            System.err.println("Ошибка сохранения задачи: " + dbError.getMessage());
            throw new RuntimeException("Не удалось сохранить задачу", dbError);
        }
    }

    // 2. НАЙТИ по ID
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
            System.err.println("Ошибка поиска задачи: " + dbError.getMessage());
            throw new RuntimeException("Не удалось найти задачу", dbError);
        }

        return Optional.empty();
    }

    // 3. ПОЛУЧИТЬ ВСЕ задачи
    @Override
    public List<Task> findAll() {
        String selectAllQuery = "SELECT * FROM tasks ORDER BY id";
        List<Task> tasksList = new ArrayList<>();

        try (Connection dbConnection = ConnectionManager.open();
             Statement sqlStatement = dbConnection.createStatement();
             ResultSet queryResult = sqlStatement.executeQuery(selectAllQuery)) {

            while (queryResult.next()) {
                Task currentTask = mapResultSetToTask(queryResult);
                tasksList.add(currentTask);
            }

        } catch (SQLException dbError) {
            System.err.println("Ошибка загрузки задач: " + dbError.getMessage());
        }

        return tasksList;
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
                System.out.println("Задача с ID " + task.getId() + " не найдена");
            }

        } catch (SQLException dbError) {
            System.err.println("Ошибка обновления задачи: " + dbError.getMessage());
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
                System.out.println("Задача " + taskId + " удалена из БД");
            } else {
                System.out.println("Задача с ID " + taskId + " не найдена в БД");
            }

        } catch (SQLException dbError) {
            System.err.println("Ошибка удаления задачи: " + dbError.getMessage());
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
                System.out.println("Статус задачи " + taskId + " обновлен на " + newStatus);
            }

        } catch (SQLException dbError) {
            System.err.println("Ошибка обновления статуса: " + dbError.getMessage());
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

