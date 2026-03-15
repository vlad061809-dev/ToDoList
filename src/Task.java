import java.time.LocalDate;

public class Task {

    private int priority;
    private LocalDate deadline;
    private int id;
    private String description;
    private String name;
    private Status status;

    public Task(int priority, LocalDate deadline, String description,
                String name, int id, Status status) {
        this.priority = priority;
        this.deadline = deadline;
        this.description = description;
        this.name = name;
        this.id = id;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "priority=" + priority +
                ", deadline=" + deadline +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

    
    




