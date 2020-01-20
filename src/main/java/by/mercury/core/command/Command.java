package by.mercury.core.command;

/**
 * Interface for command
 *
 * @author Yegor Ikbaev
 */
@FunctionalInterface
public interface Command {

    /**
     * Executes code of command
     *
     * @param context must be not null
     */
    void execute(CommandContext context);
}
