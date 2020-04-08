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
     * Retrieves an entity by its {@link UserModel#getId()} ()}.
     *
     * @param userId may be null
     * @return the entity with the given userId or {@literal Optional#empty()} if none found.
     */
    Optional<UserModel> findById(Long userId);

    /**
     * Retrieves an entity by its {@link UserModel#getPeerId()}.
     *
     * @param peerId may be null
     * @return the entity with the given peerId or {@literal Optional#empty()} if none found
     */
    Optional<UserModel> findByPeerId(Integer peerId);

    /**
     * Saves all given entities.
     *
     * @param users must not be {@literal null} nor must it contain {@literal null}.
     * @return the saved entities; will never be {@literal null}.
     * The returned {@literal Collection} will have the same size as the {@literal Collection} passed as an argument.
     * @throws IllegalArgumentException in case the given {@literal users} or one of its users is {@literal null}.
     */
    Collection<UserModel> saveAll(Collection<UserModel> users);

    /**
     * Saves given entity.
     *
     * @param user must not be {@literal null}
     * @return the saved entity; will never be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal user}
     */
    UserModel save(UserModel user);
}
