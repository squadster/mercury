package by.mercury.core.facade;

import by.mercury.core.data.MessageData;
import by.mercury.core.model.UserModel;

/**
 * Interface for message facade operations
 *
 * @author Yegor Ikbaev
 */
public interface MessageFacade {

    /**
     * Sends message to user using {@link UserModel#getPeerId()}.
     *
     * @param message must not be {@literal null}.
     * @throws by.mercury.core.exception.SendMessageException if data is wrong
     * @throws IllegalArgumentException                       if message or it's fields are null
     */
    void send(MessageData message);
}
