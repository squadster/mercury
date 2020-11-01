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
    private static final Long USER_ID = 1L;
    private static final Long SCHEDULE_ID = 2L;
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
        if (!"none".equals(peerId) && userService.findById(USER_ID).isEmpty()) {
            var user = UserModel.builder()
                    .id(USER_ID)
                    .uid(peerId)
                    .peerId(Integer.parseInt(peerId))
                    .build();
            user = userService.save(user);
            generateSquad(user);
            var schedule = generateSchedule();
            generateLesson(100L, 1, "Name 1", "Teacher 1", "Note 1", schedule);
            generateLesson(200L, 2, "Name 2", "Teacher 2", "Note 2", schedule);
            generateLesson(300L, 3, "Name 3", "Teacher 3", "Note 3", schedule);
            generateLesson(400L, 4, "Name 4", "Teacher 4", "Note 4", schedule);
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
    
    private ScheduleModel generateSchedule() {
        var schedule = ScheduleModel.builder()
                .id(SCHEDULE_ID)
                .squad(SQUAD)
                .date(LocalDate.now())
                .build();
        return scheduleDao.save(schedule);
    }
    
    private LessonModel generateLesson(Long id, Integer index, String name, 
                                       String teacher, String note, ScheduleModel schedule) {
        var lesson = LessonModel.builder()
                .id(id)
                .index(index)
                .name(name)
                .teacher(teacher)
                .note(note)
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
