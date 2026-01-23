import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Agy {
    private static final String FILE_PATH = "./data/agy.txt";

    public static void main(String[] args) {
        printMessage("Hello! I'm Agy\nWhat can I do for you?");

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = loadTasks();

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
                                saveTasks(tasks);
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
                                saveTasks(tasks);
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
                        saveTasks(tasks);
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
                        saveTasks(tasks);
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
                        saveTasks(tasks);
                        break;
                    case DELETE:
                        try {
                            int index = Integer.parseInt(input.substring(7)) - 1;
                            if (index >= 0 && index < tasks.size()) {
                                Task removedTask = tasks.remove(index);
                                saveTasks(tasks);
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

    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_PATH);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" \\| ");
                Task task = null;
                switch (parts[0]) {
                    case "T":
                        task = new Todo(parts[2]);
                        break;
                    case "D":
                        task = new Deadline(parts[2], parts[3]);
                        break;
                    case "E":
                        task = new Event(parts[2], parts[3], parts[4]);
                        break;
                }
                if (task != null) {
                    if (parts[1].equals("1")) {
                        task.markAsDone();
                    }
                    tasks.add(task);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            // File not found, start with empty list
        }
        return tasks;
    }

    private static void saveTasks(List<Task> tasks) {
        try {
            File file = new File(FILE_PATH);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            FileWriter writer = new FileWriter(file);
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            printMessage("Error saving tasks: " + e.getMessage());
        }
    }
}
