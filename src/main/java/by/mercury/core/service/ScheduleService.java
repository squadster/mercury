package by.mercury.core.service;

import by.mercury.core.model.ScheduleModel;
import by.mercury.core.model.UserModel;

import java.io.File;
import java.util.List;

public interface ScheduleService {
    
    List<ScheduleModel> getScheduleForUser(UserModel user);
    
    File generateSchedule(List<ScheduleModel> scheduleModel);
}
