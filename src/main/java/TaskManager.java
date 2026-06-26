import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j


public class TaskManager {
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
                case 5 -> syncToDatabase();
                case 0 -> {
                    log.info("Завершение работы. Сохранение задач...");
                    System.out.println("Сохранение задач в базу данных...");
                    handler.syncAllToDatabase();
                    System.out.println("Выход...");
                    running = false;
                }
                default -> System.out.println("Неверный ввод, попробуйте снова.");
            }
        }
    }

    private void addTask() {
        String title = reader.readString("Введите название задачи");
        String description = reader.readString("Введите описание");
        int priority = reader.readInt("Введите приоритет (число)");

        String dateStr = reader.readString("Введите дедлайн (гггг-мм-дд)");
        LocalDate deadline = LocalDate.parse(dateStr);

        int id = handler.getAllTasks().size() + 1;

        Task task = new Task(id, deadline, title, description, priority, Status.NEW);
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
        log.info("Пользователь отметил выполненной задачу id={}", id);
    }

    private void syncToDatabase() {
        handler.syncAllToDatabase();
        System.out.println("Синхронизация с базой данных выполнена!");
        log.info("Ручная синхронизация с БД");
    }
}