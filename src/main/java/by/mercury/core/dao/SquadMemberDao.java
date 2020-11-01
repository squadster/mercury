package by.mercury.core.dao;

import by.mercury.core.model.SquadMemberModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SquadMemberDao extends CrudRepository<SquadMemberModel, Long> {
    
    Optional<SquadMemberModel> findByUserId(Long userId);
}
