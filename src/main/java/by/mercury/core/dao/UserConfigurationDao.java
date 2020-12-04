package by.mercury.core.dao;

import by.mercury.core.model.UserConfigurationModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserConfigurationDao extends CrudRepository<UserConfigurationModel, Long> {
    
    Optional<UserConfigurationModel> findByUserId(Long userId);
}
