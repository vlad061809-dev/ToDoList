import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = ConnectionManager.open()) {

            if (connection != null) {
                System.out.println("Соединение установлено.");
                System.out.println("База данных: " + connection.getCatalog());
            }

        } catch (SQLException e) {
            System.err.println("Ошибка подключения!");
            e.printStackTrace();
        }
    }

    public void addTaskToDb(String title) {
        String sql = "INSERT INTO tasks (title) VALUES (?)";
        String url = "jdbc:postgresql://localhost:5432/todo_db";
        String user = "postgres";
        String password = "12345";

        try (Connection conn = ConnectionManager.open();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.executeUpdate();
            System.out.println("Задача сохранена в базу!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
