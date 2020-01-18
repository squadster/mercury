package by.mercury.core.service;

import by.mercury.core.model.UserModel;

import java.util.Collection;
import java.util.Optional;

/**
 * Interface for users services operations
 *
 * @author Yegor Ikbaev
 */
public interface UserService {

    /**
     * Retrieves an entity by its {@link UserModel#getPeerId()}.
     *
     * @param peerId may be null
     * @return the entity with the given peerId or {@literal Optional#empty()} if none found or peerId is null
     */
    Optional<UserModel> findByPeerId(Integer peerId);

    /**
     * Saves all given entities.
     *
     * @param users must not be {@literal null} nor must it contain {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal users} or one of its users is {@literal null}.
     */
    void saveAll(Collection<UserModel> users);
}
