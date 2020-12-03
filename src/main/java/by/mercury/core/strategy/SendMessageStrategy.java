package by.mercury.core.strategy;

import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;

import java.util.Collection;

/**
 * Strategy for sending message
 *
 * @author Yegor Ikbaev
 */
public interface SendMessageStrategy {

    boolean support(Collection<Channel> channels);
    
    boolean support(MessageType messageType);
    
    boolean support(UserModel user);
    
    /**
     * Sends message to user using {@link UserModel#getPeerId()}.
     *
     * @param message must not be {@literal null}.
     * @throws by.mercury.core.exception.SendMessageException if data is wrong
     * @throws IllegalArgumentException                       if message or it's fields are null
     */
    void send(MessageModel message);
}
