package by.mercury.core.command;

import by.mercury.core.model.Channel;

/**
 * Interface for command
 *
 * @author Yegor Ikbaev
 */
public interface Command {

    boolean support(Channel channel);
    
    boolean support(CommandContext context);
    
    /**
     * Executes code of command
     *
     * @param context must be not null
     */
    void execute(CommandContext context);
}
