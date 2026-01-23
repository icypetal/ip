package agy;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import agy.command.Command;
import agy.exception.AgyException;
import agy.parser.Parser;
import agy.storage.Storage;
import agy.task.Deadline;
import agy.task.Event;
import agy.task.Task;
import agy.task.TaskList;
import agy.task.Todo;
import agy.ui.Ui;

public class Agy {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private static final String FILE_PATH = "./data/agy.txt";

    public Agy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (AgyException e) {
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command command = Parser.parse(fullCommand);
                switch (command) {
                    case BYE:
                        ui.printMessage("Bye. Hope to see you again soon!");
                        isExit = true;
                        break;
                    case LIST:
                        String listOutput = IntStream.range(0, tasks.size())
                                .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                                .collect(Collectors.joining("\n"));
                        ui.printMessage("Here are the tasks in your list:\n" + listOutput);
                        break;
                    case MARK:
                        try {
                            int index = Integer.parseInt(fullCommand.substring(5)) - 1;
                            if (index >= 0 && index < tasks.size()) {
                                Task task = tasks.get(index);
                                task.markAsDone();
                                storage.save(tasks.getAll());
                                ui.printMessage("Nice! I've marked this task as done:\n  " + task);
                            } else {
                                throw new AgyException("Invalid task number.");
                            }
                        } catch (NumberFormatException e) {
                            throw new AgyException("Please provide a valid task number.");
                        }
                        break;
                    case UNMARK:
                        try {
                            int index = Integer.parseInt(fullCommand.substring(7)) - 1;
                            if (index >= 0 && index < tasks.size()) {
                                Task task = tasks.get(index);
                                task.markAsNotDone();
                                storage.save(tasks.getAll());
                                ui.printMessage("OK, I've marked this task as not done yet:\n  " + task);
                            } else {
                                throw new AgyException("Invalid task number.");
                            }
                        } catch (NumberFormatException e) {
                            throw new AgyException("Please provide a valid task number.");
                        }
                        break;
                    case TODO:
                        if (fullCommand.trim().length() <= 4) {
                            throw new AgyException(
                                    "Error: The description of a todo cannot be empty. Usage: todo <description>");
                        }
                        Task task = new Todo(fullCommand.substring(5));
                        tasks.add(task);
                        storage.save(tasks.getAll());
                        ui.printMessage("Got it. I've added this task:\n  " + task + "\nNow you have " + tasks.size()
                                + " tasks in the list.");
                        break;
                    case DEADLINE:
                        if (fullCommand.trim().length() <= 8) {
                            throw new AgyException(
                                    "Error: The description of a deadline cannot be empty. Usage: deadline <description> /by <time>");
                        }
                        String[] parts = fullCommand.substring(9).split(" /by ");
                        if (parts.length < 2) {
                            throw new AgyException(
                                    "Error: Dates/times cannot be empty. Usage: deadline <description> /by <time>");
                        }
                        try {
                            Task dlTask = new Deadline(parts[0], parts[1]);
                            tasks.add(dlTask);
                            storage.save(tasks.getAll());
                            ui.printMessage("Got it. I've added this task:\n  " + task(dlTask) + "\nNow you have "
                                    + tasks.size() + " tasks in the list.");
                        } catch (DateTimeParseException e) {
                            throw new AgyException(
                                    "Error: Invalid date format. Please use yyyy-mm-dd (e.g., 2019-10-15).");
                        }
                        break;
                    case EVENT:
                        if (fullCommand.trim().length() <= 5) {
                            throw new AgyException(
                                    "Error: The description of an event cannot be empty. Usage: event <description> /from <start> /to <end>");
                        }
                        String[] eventParts = fullCommand.substring(6).split(" /from ");
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
                        tasks.add(eventTask);
                        storage.save(tasks.getAll());
                        ui.printMessage("Got it. I've added this task:\n  " + task(eventTask) + "\nNow you have "
                                + tasks.size() + " tasks in the list.");
                        break;
                    case DELETE:
                        try {
                            int index = Integer.parseInt(fullCommand.substring(7)) - 1;
                            if (index >= 0 && index < tasks.size()) {
                                Task removedTask = tasks.delete(index);
                                storage.save(tasks.getAll());
                                ui.printMessage("Noted. I've removed this task:\n  " + removedTask + "\nNow you have "
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
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Agy(FILE_PATH).run();
    }

    // Helper to format string for printMessage to keep it consistent
    private String task(Task t) {
        return t.toString();
    }
}
