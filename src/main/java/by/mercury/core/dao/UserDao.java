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
     * Retrieves an entity by its {@link UserModel#getPeerId()}.
     *
     * @param peerId may be null
     * @return the entity with the given peerId or {@literal Optional#empty()} if none found
     */
    Optional<UserModel> findByUid(String peerId);
    
    Optional<UserModel> findByTelegramId(Integer telegramId);
    
    Optional<UserModel> findByTelegramToken(String token);
}
