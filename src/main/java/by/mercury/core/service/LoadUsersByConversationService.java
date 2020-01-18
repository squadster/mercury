package by.mercury.core.service;

/**
 * Interface for initial loading users from conversations
 *
 * @author Yegor Ikbaev
 */
public interface LoadUsersByConversationService {

    /**
     * Load all users and save in repository.
     */
    void load();
}
