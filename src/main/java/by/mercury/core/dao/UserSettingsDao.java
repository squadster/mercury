package by.mercury.core.dao;

import by.mercury.core.model.UserModel;
import by.mercury.core.model.UserSettingsModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserSettingsDao extends CrudRepository<UserSettingsModel, Long> {

    Optional<UserSettingsModel> findByUser(UserModel user);
}
