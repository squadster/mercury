package by.mercury.core.command;

/**
 * Interface for command
 *
 * @author Yegor Ikbaev
 */
public interface Command {

    boolean support(CommandContext context);
    
    /**
     * Executes code of command
     *
     * @param context must be not null
     */
    void execute(CommandContext context);
}
