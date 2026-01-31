package agy.ui;

import java.util.Scanner;

/**
 * Handles the user interface of the application. Manages reading specific
 * commands and displaying messages to the user.
 */
public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message to the user.
     */
    public void showWelcome() {
        printMessage("Hello! I'm Agy\nWhat can I do for you?");
    }

    /**
     * Reads a command from the standard input.
     *
     * @return The command string entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        printMessage(message);
    }

    /**
     * Displays an error message when loading tasks from file fails.
     */
    public void showLoadingError() {
        printMessage("Error loading tasks from file. Starting with an empty list.");
    }

    /**
     * Prints a message to the console wrapped in horizontal lines.
     *
     * @param message The message to print.
     */
    public void printMessage(String message) {
        showLine();
        System.out.println(" " + message.replace("\n", "\n "));
        showLine();
    }
}
