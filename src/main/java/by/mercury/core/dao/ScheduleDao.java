package by.mercury.core.dao;

import by.mercury.core.model.ScheduleModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ScheduleDao extends CrudRepository<ScheduleModel, Long> {
    
    Optional<ScheduleModel> findBySquad(Long squad);
}
