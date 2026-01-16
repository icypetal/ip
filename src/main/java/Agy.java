import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Agy {
    public static void main(String[] args) {
        printMessage("Hello! I'm Agy\nWhat can I do for you?");

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                printMessage("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) {
                String listOutput = IntStream.range(0, tasks.size())
                        .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                        .collect(Collectors.joining("\n"));
                printMessage("Here are the tasks in your list:\n" + listOutput);
            } else if (input.startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5)) - 1;
                    if (index >= 0 && index < tasks.size()) {
                        Task task = tasks.get(index);
                        task.markAsDone();
                        printMessage("Nice! I've marked this task as done:\n  " + task);
                    } else {
                        printMessage("Invalid task number.");
                    }
                } catch (NumberFormatException e) {
                    printMessage("Please provide a valid task number.");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    if (index >= 0 && index < tasks.size()) {
                        Task task = tasks.get(index);
                        task.markAsNotDone();
                        printMessage("OK, I've marked this task as not done yet:\n  " + task);
                    } else {
                        printMessage("Invalid task number.");
                    }
                } catch (NumberFormatException e) {
                    printMessage("Please provide a valid task number.");
                }
            } else {
                tasks.add(new Task(input));
                printMessage("added: " + input);
            }
        }
        scanner.close();
    }

    private static void printMessage(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(" " + message.replace("\n", "\n "));
        System.out.println("____________________________________________________________");
    }
}
