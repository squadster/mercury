package by.mercury.core.service;

import by.mercury.core.model.ScheduleModel;
import by.mercury.core.model.UserModel;

import java.io.File;

public interface ScheduleService {
    
    ScheduleModel getScheduleForUser(UserModel user);
    
    File generateSchedule(ScheduleModel scheduleModel);
}
