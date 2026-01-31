package agy.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {
    @Test
    public void testToString() {
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }
}
