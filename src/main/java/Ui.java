import java.util.Scanner;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        printMessage("Hello! I'm Agy\nWhat can I do for you?");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showError(String message) {
        printMessage(message);
    }

    public void showLoadingError() {
        printMessage("Error loading tasks from file. Starting with an empty list.");
    }

    public void printMessage(String message) {
        showLine();
        System.out.println(" " + message.replace("\n", "\n "));
        showLine();
    }
}
