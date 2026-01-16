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
            try {
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
                            throw new AgyException("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        throw new AgyException("Please provide a valid task number.");
                    }
                } else if (input.startsWith("unmark ")) {
                    try {
                        int index = Integer.parseInt(input.substring(7)) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            Task task = tasks.get(index);
                            task.markAsNotDone();
                            printMessage("OK, I've marked this task as not done yet:\n  " + task);
                        } else {
                            throw new AgyException("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        throw new AgyException("Please provide a valid task number.");
                    }
                } else if (input.startsWith("delete ")) {
                    try {
                        int index = Integer.parseInt(input.substring(7)) - 1;
                        if (index >= 0 && index < tasks.size()) {
                            Task task = tasks.remove(index);
                            printMessage("Noted. I've removed this task:\n  " + task + "\nNow you have " + tasks.size()
                                    + " tasks in the list.");
                        } else {
                            throw new AgyException("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        throw new AgyException("Please provide a valid task number.");
                    }
                } else if (input.startsWith("todo")) {
                    if (input.trim().length() <= 4) {
                        throw new AgyException(
                                "Error: The description of a todo cannot be empty. Usage: todo <description>");
                    }
                    Task task = new Todo(input.substring(5));
                    addTask(tasks, task);
                } else if (input.startsWith("deadline")) {
                    if (input.trim().length() <= 8) {
                        throw new AgyException(
                                "Error: The description of a deadline cannot be empty. Usage: deadline <description> /by <time>");
                    }
                    String[] parts = input.substring(9).split(" /by ");
                    if (parts.length < 2) {
                        throw new AgyException(
                                "Error: Dates/times cannot be empty. Usage: deadline <description> /by <time>");
                    }
                    Task task = new Deadline(parts[0], parts[1]);
                    addTask(tasks, task);
                } else if (input.startsWith("event")) {
                    if (input.trim().length() <= 5) {
                        throw new AgyException(
                                "Error: The description of an event cannot be empty. Usage: event <description> /from <start> /to <end>");
                    }
                    String[] parts = input.substring(6).split(" /from ");
                    if (parts.length < 2) {
                        throw new AgyException(
                                "Error: Missing /from or /to. Usage: event <description> /from <start> /to <end>");
                    }
                    String[] times = parts[1].split(" /to ");
                    if (times.length < 2) {
                        throw new AgyException(
                                "Error: Missing /from or /to. Usage: event <description> /from <start> /to <end>");
                    }
                    Task task = new Event(parts[0], times[0], times[1]);
                    addTask(tasks, task);
                } else {
                    throw new AgyException("Error: Unknown command");
                }
            } catch (AgyException e) {
                printMessage(e.getMessage());
            }
        }
        scanner.close();
    }

    private static void addTask(List<Task> tasks, Task task) {
        tasks.add(task);
        printMessage(
                "Got it. I've added this task:\n  " + task + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private static void printMessage(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(" " + message.replace("\n", "\n "));
        System.out.println("____________________________________________________________");
    }
}
