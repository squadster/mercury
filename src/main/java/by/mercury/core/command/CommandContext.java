package by.mercury.core.command;

import by.mercury.core.model.MessageModel;

import java.util.Map;

/**
 * Describes context of received message
 *
 * @author Yegor Ikbaev
 */
public interface CommandContext {

    MessageModel getMessage();

    Map<String, Object> getParameters();
}
