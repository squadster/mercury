package by.mercury.core.command;

import by.mercury.core.model.MessageModel;

/**
 * Describes context of received message
 *
 * @author Yegor Ikbaev
 */
public interface CommandContext {
    MessageModel getMessage();
}
