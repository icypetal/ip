package agy.parser;

import agy.command.Command;
import agy.exception.AgyException;

public class Parser {
    public static Command parse(String fullCommand) throws AgyException {
        String commandStr = fullCommand.split(" ")[0].toUpperCase();
        try {
            return Command.valueOf(commandStr);
        } catch (IllegalArgumentException e) {
            throw new AgyException("Error: Unknown command");
        }
    }
}
