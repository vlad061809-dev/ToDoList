import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ToDoList {
    private static List<String> tasks = new ArrayList<>();

    public static void main(String[] args) {

        printMenu();
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        boolean isExit = true;
        while (isExit) {
            if (choice == 1) {
                taskAdd(scanner);
            }
            if (choice == 2) {
                taskShow();
            }
            if (choice == 3) {
                taskDelete(scanner);
            }
            if (choice == 4){
                taskComplete(scanner);
            }
            if (choice == 0) {
                System.out.println("Выхожу из приложения");
                break;
            }
            printMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

        }
    }

    private static void printMenu() {
        String[] commands = {"Добавить задачу", "Показать все задачи", "Удалить задачу (по номеру)", "Отметить задачу как выполненную", "Выход"};

        System.out.println(">>> Меню:");
        System.out.println("1. " + commands[0]);
        System.out.println("2. " + commands[1]);
        System.out.println("3. " + commands[2]);
        System.out.println("4. " + commands[3]);
        System.out.println("0. " + commands[4]);
        System.out.println("Выберите пункт меню:");
    }

    public static String taskAdd(Scanner scanner) {
        System.out.println("Введите задачу: ");
        String task = scanner.nextLine();
        System.out.println("Задача \"" + task + "\" добавлена");
        tasks.add(task);
        return task;
    }

    public static void taskShow() {
        System.out.println("Ваши задачи: " + tasks);
    }

    public static void taskDelete(Scanner scanner) {
        System.out.println("Выберете задачу для удаления: " + tasks);
        int indexOfTask = scanner.nextInt();
        if (indexOfTask >= 0 && indexOfTask < tasks.size()) {
            tasks.remove(indexOfTask);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Неверный номер");
        }
    }
            public static void taskComplete(Scanner scanner) {
                System.out.println("Выберете задачу, которую выполнили: " + tasks);
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i+1) + ". " + tasks.get(i));
                }
                int indexOfTask = scanner.nextInt() - 1;
                scanner.nextLine();

                if (indexOfTask >= 0 && indexOfTask < tasks.size()) {
                    String oldTask = tasks.get(indexOfTask);
                    String newTask = "[✓]" + oldTask;
                    tasks.set(indexOfTask,newTask);
                    System.out.println("Задача отмечена, как выполненная [✓] " );
                }else{
                    System.out.println("Неверный номер");
            }
        }
    }





