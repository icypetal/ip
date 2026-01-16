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

        boolean isExit = false;
        while (!isExit) {
            String input = scanner.nextLine();
            try {
                String commandStr = input.split(" ")[0].toUpperCase();
                Command command;
                try {
                    command = Command.valueOf(commandStr);
                } catch (IllegalArgumentException e) {
                    throw new AgyException("Error: Unknown command");
                }

                switch (command) {
                    case BYE:
                        printMessage("Bye. Hope to see you again soon!");
                        isExit = true;
                        break; // Exit loop
                    case LIST:
                        String listOutput = IntStream.range(0, tasks.size())
                                .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                                .collect(Collectors.joining("\n"));
                        printMessage("Here are the tasks in your list:\n" + listOutput);
                        break;
                    case MARK:
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
                        break;
                    case UNMARK:
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
                        break;
                    case TODO:
                        if (input.trim().length() <= 4) {
                            throw new AgyException(
                                    "Error: The description of a todo cannot be empty. Usage: todo <description>");
                        }
                        Task task = new Todo(input.substring(5));
                        addTask(tasks, task);
                        break;
                    case DEADLINE:
                        if (input.trim().length() <= 8) {
                            throw new AgyException(
                                    "Error: The description of a deadline cannot be empty. Usage: deadline <description> /by <time>");
                        }
                        String[] parts = input.substring(9).split(" /by ");
                        if (parts.length < 2) {
                            throw new AgyException(
                                    "Error: Dates/times cannot be empty. Usage: deadline <description> /by <time>");
                        }
                        Task dlTask = new Deadline(parts[0], parts[1]);
                        addTask(tasks, dlTask);
                        break;
                    case EVENT:
                        if (input.trim().length() <= 5) {
                            throw new AgyException(
                                    "Error: The description of an event cannot be empty. Usage: event <description> /from <start> /to <end>");
                        }
                        String[] eventParts = input.substring(6).split(" /from ");
                        if (eventParts.length < 2) {
                            throw new AgyException(
                                    "Error: Missing /from or /to. Usage: event <description> /from <start> /to <end>");
                        }
                        String[] times = eventParts[1].split(" /to ");
                        if (times.length < 2) {
                            throw new AgyException(
                                    "Error: Missing /from or /to. Usage: event <description> /from <start> /to <end>");
                        }
                        Task eventTask = new Event(eventParts[0], times[0], times[1]);
                        addTask(tasks, eventTask);
                        break;
                    case DELETE:
                        try {
                            int index = Integer.parseInt(input.substring(7)) - 1;
                            if (index >= 0 && index < tasks.size()) {
                                Task removedTask = tasks.remove(index);
                                printMessage("Noted. I've removed this task:\n  " + removedTask + "\nNow you have "
                                        + tasks.size() + " tasks in the list.");
                            } else {
                                throw new AgyException("Invalid task number.");
                            }
                        } catch (NumberFormatException e) {
                            throw new AgyException("Please provide a valid task number.");
                        }
                        break;
                    default:
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
