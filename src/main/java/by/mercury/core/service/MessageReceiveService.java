package by.mercury.core.service;

import java.util.List;

/**
 * Interface for receiving messages
 *
 * @param <T> the type of message
 * @author Yegor Ikbaev
 */
public interface MessageReceiveService<T> {

    /**
     * Retrieves all messages
     *
     * @return received messages
     */
    List<T> receiveAll();
}
