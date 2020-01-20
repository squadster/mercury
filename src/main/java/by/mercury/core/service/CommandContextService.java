package by.mercury.core.service;

import by.mercury.core.command.CommandContext;

/**
 * Interface for creating {@link CommandContext} from message
 *
 * @param <T> the type of message
 * @author Yegor Ikbaev
 */
public interface CommandContextService<T> {

    /**
     * Retrieves an context of message
     *
     * @param message must be not null
     * @return the entity with the message and other data about message
     * @throws IllegalArgumentException in case the given {@literal message} is null
     */
    CommandContext build(T message);
}
