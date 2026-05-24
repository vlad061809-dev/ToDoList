import java.util.ArrayList;
import java.util.List;

public class DatabaseSyncService {
    private final TaskDatabaseDAO dbDAO = new TaskDatabaseDAO();

    public void saveToDatabase(Task task) {
        try {
            dbDAO.save(task);
            System.out.println("Задача синхронизирована с БД");
        } catch (Exception e) {
            System.err.println("Ошибка синхронизации с БД: " + e.getMessage());
        }
    }

    public List<Task> saveAllToDatabase(List<Task> tasks) {
        try {
            for (Task task : tasks) {
                dbDAO.save(task);
            }
            System.out.println("Все задачи синхронизированы с БД");
        } catch (Exception e) {
            System.err.println("Ошибка синхронизации: " + e.getMessage());
        }
        return tasks;
    }

        public List<Task> loadFromDatabase () {
            List<Task> loadedTasks = new ArrayList<>();
            try {
                loadedTasks = dbDAO.findAll();
                System.out.println("Загружено " + loadedTasks.size() + " задач из БД");
            } catch (Exception error) {
                System.err.println("Ошибка при загрузке данных: " + error.getMessage());
            }
            return loadedTasks;
        }
    }

