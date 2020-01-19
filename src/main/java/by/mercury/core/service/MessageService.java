package by.mercury.core.service;

import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;

/**
 * Interface for message service operations
 *
 * @author Yegor Ikbaev
 */
public interface MessageService {

    /**
     * Sends message to user using {@link UserModel#getPeerId()}.
     *
     * @param message must not be {@literal null}.
     * @throws by.mercury.core.exception.SendMessageException if data is wrong
     * @throws IllegalArgumentException                       if message or it's fields are null
     */
    void send(MessageModel message);
}
