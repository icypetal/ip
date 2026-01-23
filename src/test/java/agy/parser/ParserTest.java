package agy.parser;

import agy.command.Command;
import agy.exception.AgyException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    @Test
    public void parse_validCommand_success() throws AgyException {
        assertEquals(Command.TODO, Parser.parse("todo read book"));
        assertEquals(Command.DEADLINE, Parser.parse("deadline return book /by 2025-12-12"));
        assertEquals(Command.BYE, Parser.parse("bye"));
    }

    @Test
    public void parse_invalidCommand_exceptionThrown() {
        assertThrows(AgyException.class, () -> Parser.parse("notacommand"));
    }
}
