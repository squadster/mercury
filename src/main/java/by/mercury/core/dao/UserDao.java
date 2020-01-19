package by.mercury.core.dao;

import by.mercury.core.model.UserModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Interface for users CRUD operations on a repository
 *
 * @author Yegor Ikbaev
 */
public interface UserDao extends CrudRepository<UserModel, Long> {

    /**
     * Retrieves an entity by its {@link UserModel#getUserId()}.
     *
     * @param userId may be null
     * @return the entity with the given userId or {@literal Optional#empty()} if none found.
     */
    Optional<UserModel> findByUserId(Long userId);

    /**
     * Retrieves an entity by its {@link UserModel#getPeerId()}.
     *
     * @param peerId may be null
     * @return the entity with the given peerId or {@literal Optional#empty()} if none found
     */
    Optional<UserModel> findByPeerId(Integer peerId);
}
