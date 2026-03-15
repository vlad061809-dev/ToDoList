import java.util.Scanner;

public class UserInputReader {
    private final Scanner scanner = new Scanner(System.in);

    public String readString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        System.out.print(prompt + ": ");
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка! Введите число.");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}

