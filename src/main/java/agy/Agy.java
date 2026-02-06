package agy;

import java.time.format.DateTimeParseException;
import java.util.List;
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

/**
 * The main class for the Agy application. Initializes the application and
 * handles the main event loop.
 */
public class Agy {
    private static final String FILE_PATH = "./data/agy.txt";
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Initializes the Agy application with the specified file path for storage.
     *
     * @param filePath The file path to load/save tasks.
     */
    public Agy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (AgyException e) {
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main application loop. Handles user input by delegating to
     * getResponse.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            String response = getResponse(fullCommand);
            ui.printMessage(response);
            if (fullCommand.trim().equalsIgnoreCase("bye")) {
                isExit = true;
            }
        }
    }

    /**
     * The entry point of the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Agy(FILE_PATH).run();
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input The user input command.
     * @return The response string to be displayed to the user.
     */
    public String getResponse(String input) {
        assert input != null : "Input to getResponse cannot be null";
        try {
            Command command = Parser.parse(input);
            switch (command) {
            case BYE:
                return handleBye();
            case LIST:
                return handleList();
            case MARK:
                return handleMark(input);
            case UNMARK:
                return handleUnmark(input);
            case FIND:
                return handleFind(input);
            case TODO:
                return handleTodo(input);
            case DEADLINE:
                return handleDeadline(input);
            case EVENT:
                return handleEvent(input);
            case DELETE:
                return handleDelete(input);
            case TAG:
                return handleTag(input);
            default:
                return "Error: Unknown command";
            }
        } catch (AgyException e) {
            return e.getMessage();
        }
    }

    private String handleBye() {
        return "Bye. Hope to see you again soon!";
    }

    private String handleList() {
        if (tasks.size() == 0) {
            return "Your task list is empty.";
        }
        String listOutput = IntStream.range(0, tasks.size()).mapToObj(i -> (i + 1) + "." + tasks.get(i))
                .collect(Collectors.joining("\n"));
        return "Here are the tasks in your list:\n" + listOutput;
    }

    private String handleMark(String input) throws AgyException {
        try {
            int index = Integer.parseInt(input.substring(5)) - 1;
            validateIndex(index);
            Task task = tasks.get(index);
            task.markAsDone();
            storage.save(tasks.getAll());
            return "Nice! I've marked this task as done:\n" + "  " + task;
        } catch (NumberFormatException e) {
            throw new AgyException("Please provide a valid task number.");
        }
    }

    private String handleUnmark(String input) throws AgyException {
        try {
            int index = Integer.parseInt(input.substring(7)) - 1;
            validateIndex(index);
            Task task = tasks.get(index);
            task.markAsNotDone();
            storage.save(tasks.getAll());
            return "OK, I've marked this task as not done yet:\n" + "  " + task;
        } catch (NumberFormatException e) {
            throw new AgyException("Please provide a valid task number.");
        }
    }

    private String handleFind(String input) throws AgyException {
        if (input.trim().length() <= 4) {
            throw new AgyException("Error: The keyword cannot be empty. Usage: find <keyword>");
        }
        String keyword = input.substring(5).trim();
        List<Task> foundTasks = tasks.findTasks(keyword);
        if (foundTasks.isEmpty()) {
            return "No matching tasks found.";
        }
        String foundListOutput = IntStream.range(0, foundTasks.size()).mapToObj(i -> (i + 1) + "." + foundTasks.get(i))
                .collect(Collectors.joining("\n"));
        return "Here are the matching tasks in your list:\n" + foundListOutput;
    }

    private String handleTodo(String input) throws AgyException {
        if (input.trim().length() <= 4) {
            throw new AgyException("Error: The description of a todo cannot be empty. Usage: todo <description>");
        }
        Task task = new Todo(input.substring(5));
        return addTask(task);
    }

    private String handleDeadline(String input) throws AgyException {
        if (input.trim().length() <= 8) {
            throw new AgyException("Error: The description of a deadline cannot be empty. "
                    + "Usage: deadline <description> /by <time>");
        }
        String[] parts = input.substring(9).split(" /by ");
        if (parts.length < 2) {
            throw new AgyException("Error: Dates/times cannot be empty. Usage: deadline <description> /by <time>");
        }
        try {
            Task task = new Deadline(parts[0], parts[1]);
            return addTask(task);
        } catch (DateTimeParseException e) {
            throw new AgyException("Error: Invalid date format. Please use yyyy-mm-dd (e.g., 2019-10-15).");
        }
    }

    private String handleEvent(String input) throws AgyException {
        if (input.trim().length() <= 5) {
            throw new AgyException("Error: The description of an event cannot be empty. "
                    + "Usage: event <description> /from <start> /to <end>");
        }
        String[] eventParts = input.substring(6).split(" /from ");
        if (eventParts.length < 2) {
            throw new AgyException("Error: Missing /from or /to. Usage: event <description> /from <start> /to <end>");
        }
        String[] times = eventParts[1].split(" /to ");
        if (times.length < 2) {
            throw new AgyException("Error: Missing /from or /to. Usage: event <description> /from <start> /to <end>");
        }
        Task task = new Event(eventParts[0], times[0], times[1]);
        return addTask(task);
    }

    private String handleDelete(String input) throws AgyException {
        try {
            int index = Integer.parseInt(input.substring(7)) - 1;
            validateIndex(index);
            Task removedTask = tasks.delete(index);
            storage.save(tasks.getAll());
            return "Noted. I've removed this task:\n" + "  " + removedTask + "\nNow you have " + tasks.size()
                    + " tasks in the list.";
        } catch (NumberFormatException e) {
            throw new AgyException("Please provide a valid task number.");
        }
    }

    private String addTask(Task task) throws AgyException {
        tasks.add(task);
        storage.save(tasks.getAll());
        return "Got it. I've added this task:\n" + "  " + task + "\nNow you have " + tasks.size()
                + " tasks in the list.";
    }

    private String handleTag(String input) throws AgyException {
        String[] parts = input.split(" ");
        if (parts.length < 3) {
            throw new AgyException("Error: Invalid tag command. Usage: tag <task index> <tag name>");
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            validateIndex(index);
            Task task = tasks.get(index);
            String tag = parts[2];
            if (tag.startsWith("#")) {
                tag = tag.substring(1);
            }
            task.addTag(tag);
            storage.save(tasks.getAll());
            return "Nice! I've added the tag #" + tag + " to this task:\n" + "  " + task;
        } catch (NumberFormatException e) {
            throw new AgyException("Please provide a valid task number.");
        }
    }

    private void validateIndex(int index) throws AgyException {
        if (index < 0 || index >= tasks.size()) {
            throw new AgyException("Invalid task number.");
        }
    }
}
