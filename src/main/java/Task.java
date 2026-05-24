import java.time.LocalDate;

public class Task {


    private int priority;
    private LocalDate deadline;
    private int id;
    private String description;
    private String title;
    private Status status;
    private boolean completed;

    public Task(int priority, LocalDate deadline, String description,
                String title, int id, Status status) {
        this.priority = priority;
        this.deadline = deadline;
        this.description = description;
        this.title = title;
        this.id = id;
        this.status = Status.NEW;
    }

    public Task(int id, String title, boolean isCompleted) {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

    
    




