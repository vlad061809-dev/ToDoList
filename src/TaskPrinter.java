import java.util.List;

public final class TaskPrinter {
    private TaskPrinter() {
        throw new UnsupportedOperationException();
    }

    public static void printMenu() {
        String[] commands = {"Добавить задачу", "Показать все задачи", "Удалить задачу (по номеру)", "Отметить задачу как выполненную", "Выход"};

        System.out.println(">>> Меню:");
        System.out.println("1. " + commands[0]);
        System.out.println("2. " + commands[1]);
        System.out.println("3. " + commands[2]);
        System.out.println("4. " + commands[3]);
        System.out.println("0. " + commands[4]);
        System.out.println("Выберите пункт меню:");
    }

    public static void printAll(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }
        tasks.stream()
                .forEach(System.out::println);

    }
}
