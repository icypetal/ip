package agy.task;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected Set<String> tags;

    /**
     * Creates a new Task with the given description.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.tags = new HashSet<>();
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Adds a tag to the task.
     *
     * @param tag The tag to add.
     */
    public void addTag(String tag) {
        tags.add(tag);
    }

    /**
     * Removes a tag from the task.
     *
     * @param tag The tag to remove.
     */
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    @Override
    public String toString() {
        String tagsString = tags.isEmpty() ? ""
                : " " + tags.stream().map(t -> "#" + t).collect(Collectors.joining(" "));
        return "[" + getStatusIcon() + "] " + description + tagsString;
    }

    public String toFileFormat() {
        String tagsString = tags.isEmpty() ? "" : " | " + String.join(" ", tags);
        return " | " + (isDone ? "1" : "0") + " | " + description + tagsString;
    }
}
