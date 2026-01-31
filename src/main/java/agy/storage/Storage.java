package agy.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import agy.exception.AgyException;
import agy.task.Deadline;
import agy.task.Event;
import agy.task.Task;
import agy.task.Todo;

/**
 * Handles the loading and saving of tasks to a file.
 */
public class Storage {
    private String filePath;

    /**
     * Initializes the Storage with the specified file path.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the file.
     *
     * @return A list of tasks loaded from the file.
     * @throws AgyException If an error occurs while parsing the file.
     */
    public List<Task> load() throws AgyException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
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
                    try {
                        task = new Deadline(parts[2], parts[3]);
                    } catch (DateTimeParseException e) {
                        // Skip corrupted date
                    }
                    break;
                case "E":
                    task = new Event(parts[2], parts[3], parts[4]);
                    break;
                default:
                    // Unknown task type, skip
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

    /**
     * Saves the current list of tasks to the file.
     *
     * @param tasks The list of tasks to save.
     * @throws AgyException If an I/O error occurs while writing to the file.
     */
    public void save(List<Task> tasks) throws AgyException {
        try {
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            FileWriter writer = new FileWriter(file);
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new AgyException("Error saving tasks: " + e.getMessage());
        }
    }
}
