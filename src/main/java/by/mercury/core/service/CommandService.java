package by.mercury.core.service;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;

/**
 * Interface for resolving {@link Command} from {@link CommandContext}
 *
 * @author Yegor Ikbaev
 */
public interface CommandService {

    /**
     * Retrieves an command from context of message
     *
     * @param context must be not null
     * @return the implementation of {@link Command}
     */
    Command resolve(CommandContext context);
    
    Command getCommandOnError();
}
