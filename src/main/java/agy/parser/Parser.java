package agy.parser;

import agy.command.Command;
import agy.exception.AgyException;

/**
 * Parses user input commands.
 */
public class Parser {
    /**
     * Parses the full user command and returns the corresponding Command enum.
     *
     * @param fullCommand The full command string entered by the user.
     * @return The Command enum corresponding to the user input.
     * @throws AgyException If the command is unknown.
     */
    public static Command parse(String fullCommand) throws AgyException {
        String commandStr = fullCommand.split(" ")[0].toUpperCase();
        try {
            return Command.valueOf(commandStr);
        } catch (IllegalArgumentException e) {
            throw new AgyException("Error: Unknown command");
        }
    }
}
