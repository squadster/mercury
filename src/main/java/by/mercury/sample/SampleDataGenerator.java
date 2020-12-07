package by.mercury.sample;

import by.mercury.core.dao.LessonDao;
import by.mercury.core.dao.ScheduleDao;
import by.mercury.core.dao.SquadMemberDao;
import by.mercury.core.model.LessonModel;
import by.mercury.core.model.ScheduleModel;
import by.mercury.core.model.SquadMemberModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SampleDataGenerator implements ApplicationRunner, Ordered {

    private static final int PRIORITY = Ordered.LOWEST_PRECEDENCE;
    private static final Long SQUAD = 721700L;
    
    private UserService userService;
    
    private ScheduleDao scheduleDao;
    
    private SquadMemberDao squadMemberDao;
    
    private LessonDao lessonDao;
    
    private String peerId;

    public SampleDataGenerator(UserService userService, ScheduleDao scheduleDao, 
                               SquadMemberDao squadMemberDao, LessonDao lessonDao) {
        this.userService = userService;
        this.scheduleDao = scheduleDao;
        this.squadMemberDao = squadMemberDao;
        this.lessonDao = lessonDao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!"none".equals(peerId) && userService.findById(Long.valueOf(peerId)).isEmpty()) {
            var user = UserModel.builder()
                    .id(Long.valueOf(peerId))
                    .uid(peerId)
                    .peerId(Integer.parseInt(peerId))
                    .build();
            user = userService.save(user);
            userService.updateNotificationsSettings(user,  settings -> settings.setEnableNotificationsTelegram(true));
            userService.updateNotificationsSettings(user,  settings -> settings.setEnableNotificationsVk(true));
            generateSquad(user);
            var schedule1 = generateSchedule(2L);
            generateLesson(100L, 1, "Название 1", "Преподаватель 1", "Заметка 1", "Тип 1", "1", schedule1);
            generateLesson(200L, 2, "Название 2", "Преподаватель 2", "Заметка 2", "Тип 2", "2", schedule1);
            generateLesson(300L, 3, "Название 3", "Преподаватель 3", "Заметка 3", "Тип 3", "3", schedule1);
            generateLesson(400L, 4, "Название 4", "Преподаватель 4", "Заметка 4", "Тип 4", "4", schedule1);

            var schedule2 = generateSchedule(3L);
            generateLesson(101L, 1, "Название 1", "Преподаватель 1", "Заметка 1", "Тип 1", "1", schedule2);
            generateLesson(201L, 2, "Название 2", "Преподаватель 2", "Заметка 2", "Тип 2", "2", schedule2);
            generateLesson(301L, 3, "Название 3", "Преподаватель 3", "Заметка 3", "Тип 3", "3", schedule2);
            generateLesson(401L, 4, "Название 4", "Преподаватель 4", "Заметка 4", "Тип 4", "4", schedule2);
        }
    }
    
    private SquadMemberModel generateSquad(UserModel user) {
        var squad = SquadMemberModel.builder()
                .id(SQUAD)
                .squadId(SQUAD)
                .userId(user.getId())
                .build();
        return squadMemberDao.save(squad);
    }
    
    private ScheduleModel generateSchedule(Long scheduleId) {
        var schedule = ScheduleModel.builder()
                .id(scheduleId)
                .squad(SQUAD)
                .date(LocalDate.now())
                .build();
        return scheduleDao.save(schedule);
    }
    
    private LessonModel generateLesson(Long id, Integer index, String name, String teacher, String note, 
                                       String type, String classroom, ScheduleModel schedule) {
        var lesson = LessonModel.builder()
                .id(id)
                .index(index)
                .name(name)
                .teacher(teacher)
                .note(note)
                .type(type)
                .classroom(classroom)
                .schedule(schedule)
                .build();
        return lessonDao.save(lesson);
    }

    @Override
    public int getOrder() {
        return PRIORITY;
    }

    @Value("${sample.user.peer.id}")
    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }
}
