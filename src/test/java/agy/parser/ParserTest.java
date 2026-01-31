package agy.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import agy.command.Command;
import agy.exception.AgyException;

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
