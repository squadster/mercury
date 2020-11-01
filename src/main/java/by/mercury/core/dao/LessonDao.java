package by.mercury.core.dao;

import by.mercury.core.model.LessonModel;
import org.springframework.data.repository.CrudRepository;

public interface LessonDao extends CrudRepository<LessonModel, Long> {
}
