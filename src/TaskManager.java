import java.time.LocalDate;

public class TaskManager {
    // Теперь вместо прямого Scanner используем наш UserInputReader
    private final UserInputReader reader = new UserInputReader();
    private final TaskHandler handler = new TaskHandler();

    public void start() {
        boolean running = true;
        System.out.println("Добро пожаловать в Todo List!");

        while (running) {
            TaskPrinter.printMenu();
            int action = reader.readInt("Введите номер команды");

            switch (action) {
                case 1 -> addTask();
                case 2 -> TaskPrinter.printAll(handler.getAllTasks());
                case 3 -> removeTask();
                case 4 -> completeTask();
                case 0 -> {
                    System.out.println("Выход...");
                    running = false;
                }
                default -> System.out.println("Неверный ввод, попробуйте снова.");
            }
        }
    }

    private void addTask() {
        String name = reader.readString("Введите название задачи");
        String description = reader.readString("Введите описание");
        int priority = reader.readInt("Введите приоритет (число)");

        // Ввод даты можно вынести в Reader, но для начала оставим здесь
        String dateStr = reader.readString("Введите дедлайн (гггг-мм-дд)");
        LocalDate deadline = LocalDate.parse(dateStr);

        int id = handler.getAllTasks().size() + 1;

        Task task = new Task(id, deadline, name, description, priority, Status.NEW);
        handler.addTask(task);
        System.out.println("Задача добавлена!");
    }

    private void removeTask() {
        int id = reader.readInt("Введите ID задачи для удаления");
        handler.removeTask(id);
    }

    private void completeTask() {
        int id = reader.readInt("Введите ID задачи для отметки 'Выполнено'");
        handler.completeTask(id);
    }
}