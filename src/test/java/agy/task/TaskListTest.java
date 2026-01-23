package agy.task;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskListTest {

    @Test
    public void findTasks_matchingKeyword_returnsCorrectTasks() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("read book"));
        taskList.add(new Todo("return book"));
        taskList.add(new Todo("eat lunch"));

        List<Task> found = taskList.findTasks("book");
        assertEquals(2, found.size());
        assertEquals("[T][ ] read book", found.get(0).toString());
        assertEquals("[T][ ] return book", found.get(1).toString());
    }

    @Test
    public void findTasks_noMatch_returnsEmptyList() {
        TaskList taskList = new TaskList();
        taskList.add(new Todo("read book"));

        List<Task> found = taskList.findTasks("gym");
        assertEquals(0, found.size());
    }
}
