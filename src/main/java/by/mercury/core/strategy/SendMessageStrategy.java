package by.mercury.core.strategy;

import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;

/**
 * Strategy for sending message
 *
 * @author Yegor Ikbaev
 */
public interface SendMessageStrategy {

    /**
     * Sends message to user using {@link UserModel#getPeerId()}.
     *
     * @param message must not be {@literal null}.
     * @throws by.mercury.core.exception.SendMessageException if data is wrong
     * @throws IllegalArgumentException                       if message or it's fields are null
     */
    void send(MessageModel message);
}
