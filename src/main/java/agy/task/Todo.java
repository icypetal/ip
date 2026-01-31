package agy.task;

/**
 * Represents a Todo task.
 */
public class Todo extends Task {
    /**
     * Creates a new Todo task with the given description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toFileFormat() {
        return "T" + super.toFileFormat();
    }
}
