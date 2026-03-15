import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskHandler {

    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }

    public void completeTask(int id) {
        tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .ifPresent(task -> task.setStatus(Status.DONE));

    }

    public void completeTask(String name) {
        tasks.stream()
                .filter(task -> Objects.equals(toLowerCase(name), toLowerCase(task.getName())))
                .findFirst()
                .ifPresent(task -> task.setStatus(Status.DONE));

    }

    public void methodName(int a, String b) {
    }

    public List<Task> findTasksContainsName(String name) {
        return tasks.stream()
                .filter(task -> contains(task.getName(), name))
                .toList();


    }

    private String toLowerCase(String name) {
        return name == null
                ? null
                : name.toLowerCase();
    }

    private boolean contains(String name, String toSearch) {
        return name != null && toLowerCase(name).contains(toLowerCase(toSearch));
    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}

