import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j

public  class TaskHandler {

    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;
    private final DatabaseSyncService syncService = new DatabaseSyncService();

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
            log.info("Загружено {} задач в память, nextId={}", tasks.size(), nextId);
        } else {
            log.info("База данных пуста, стартуем с пустого списка");
        }
    }

    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.add(task);
        syncService.saveToDatabase(task);
        log.info("Задача добавлена! (сохранена в память и БД)");
    }

    public void removeTask(int id) {
        Task taskToRemove = findTaskById(id);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);

            try {
                TaskDatabaseDAO dbDAO = new TaskDatabaseDAO();
                dbDAO.delete(id);
                log.info("Задача удалена: id={}", id);
            } catch (Exception error) {
                log.error("Ошибка удаления из БД: {}", error.getMessage());
            }
        } else {
            log.warn("Задача с ID {} не найдена", id);
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
                log.error("Ошибка обновления в БД: {}", error.getMessage());
            }
        } else {
            log.warn("Задача с ID {} не найдена", id);
        }
    }

    public void completeTask(String title) {
        Task taskToComplete = findTaskByTitle(title);
        if (taskToComplete != null) {
            taskToComplete.setStatus(Status.DONE);

            try {
                TaskDatabaseDAO dbDAO = new TaskDatabaseDAO();
                dbDAO.updateStatus(taskToComplete.getId(), Status.DONE);
                log.info("Задача '{}' отмечена как выполненная", title);
            } catch (Exception error) {
                log.error(" Ошибка обновления в БД: {}", error.getMessage());
            }
        } else {
            log.warn("Задача с названием '{}' не найдена", title);
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


