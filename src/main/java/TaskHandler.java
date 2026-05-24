import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskHandler {

    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;
    private final DatabaseSyncService syncService = new DatabaseSyncService();

    // Конструктор - загружает задачи из БД при запуске
    public TaskHandler() {
        loadTasksFromDatabase();
    }

    private void loadTasksFromDatabase() {
        List<Task> loadedTasks = syncService.loadFromDatabase();
        if (!loadedTasks.isEmpty()) {
            this.tasks = loadedTasks;
            this.nextId = tasks.stream()
                    .mapToInt(Task::getId)
                    .max()
                    .orElse(0) + 1;
            System.out.println("Загружено " + tasks.size() + " задач из базы данных");
        } else {
            System.out.println("База данных пуста, начинаем с чистого списка");
        }
    }

    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.add(task);
        syncService.saveToDatabase(task);
        System.out.println("Задача добавлена! (сохранена в память и БД)");
    }

    public void removeTask(int id) {
        Task taskToRemove = findTaskById(id);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);

            try {
                TaskDatabaseDAO dbDAO = new TaskDatabaseDAO();
                dbDAO.delete(id);
                System.out.println("Задача удалена из памяти и БД");
            } catch (Exception error) {
                System.err.println("Ошибка удаления из БД: " + error.getMessage());
            }
        } else {
            System.out.println("Задача с ID " + id + " не найдена");
        }
    }


    public void completeTask(int id) {
        Task taskToComplete = findTaskById(id);
        if (taskToComplete != null) {
            taskToComplete.setStatus(Status.DONE);

            try {
                TaskDatabaseDAO dbDAO = new TaskDatabaseDAO();
                dbDAO.updateStatus(id, Status.DONE);
                System.out.println("Задача отмечена как выполненная (обновлено в памяти и БД)");
            } catch (Exception error) {
                System.err.println("Ошибка обновления в БД: " + error.getMessage());
            }
        } else {
            System.out.println("Задача с ID " + id + " не найдена");
        }
    }

    public void completeTask(String title) {
        Task taskToComplete = findTaskByTitle(title);
        if (taskToComplete != null) {
            taskToComplete.setStatus(Status.DONE);

            try {
                TaskDatabaseDAO dbDAO = new TaskDatabaseDAO();
                dbDAO.updateStatus(taskToComplete.getId(), Status.DONE);
                System.out.println("Задача '" + title + "' отмечена как выполненная");
            } catch (Exception error) {
                System.err.println(" Ошибка обновления в БД: " + error.getMessage());
            }
        } else {
            System.out.println("Задача с названием '" + title + "' не найдена");
        }
    }

    public List<Task> findTasksContainsTitle(String searchText) {
        String toSearch = searchText;  // переменная toSearch
        return tasks.stream()
                .filter(task -> containsIgnoreCase(task.getTitle(), toSearch))
                .toList();
    }

    private Task findTaskById(int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Task findTaskByTitle(String title) {
        String toSearch = title;  // переменная toSearch
        return tasks.stream()
                .filter(task -> task.getTitle() != null &&
                        task.getTitle().equalsIgnoreCase(toSearch))
                .findFirst()
                .orElse(null);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public void syncAllToDatabase() {
        syncService.saveAllToDatabase(tasks);
    }

    private boolean containsIgnoreCase(String text, String searchText) {
        if (text == null || searchText == null) {
            return false;
        }
        return text.toLowerCase().contains(searchText.toLowerCase());
    }

    private String toLowerCase(String input) {
        return input == null ? null : input.toLowerCase();
    }

    private boolean contains(String text, String searchText) {
        return text != null && toLowerCase(text).contains(toLowerCase(searchText));
    }
}


