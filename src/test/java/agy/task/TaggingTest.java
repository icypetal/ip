package agy.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaggingTest {
    @Test
    public void addTag_normalTag_tagAdded() {
        Task task = new Todo("read book");
        task.addTag("fun");
        assertTrue(task.toString().contains("#fun"));
    }

    @Test
    public void toString_withTags_correctFormat() {
        Task task = new Todo("read book");
        task.addTag("fun");
        task.addTag("urgent");
        String output = task.toString();
        // Since Set order is not guaranteed, check containment
        assertTrue(output.contains("[T][ ] read book"));
        assertTrue(output.contains("#fun"));
        assertTrue(output.contains("#urgent"));
    }

    @Test
    public void toFileFormat_withTags_correctFormat() {
        Task task = new Todo("read book");
        task.addTag("fun");
        String output = task.toFileFormat();
        // Expected: T | 0 | read book | fun
        assertTrue(output.contains("T | 0 | read book"));
        assertTrue(output.contains(" | fun"));
    }
}
