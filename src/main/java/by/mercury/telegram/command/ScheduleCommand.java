package by.mercury.telegram.command;

import by.mercury.core.model.UserModel;
import by.mercury.core.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class ScheduleCommand extends TelegramAbstractCommand {

    private final ScheduleService scheduleService;
    
    public ScheduleCommand(ScheduleService scheduleService) {
        super("schedule", "Получить расписание");
        this.scheduleService = scheduleService;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        getRootUser(user).ifPresentOrElse(rootUser -> sendSchedule(rootUser, sender, chat),
                () -> sendNoScheduleMessage(sender, chat));
        log.info("Schedule command for TELEGRAM was successfully completed");

    }

    private void sendSchedule(UserModel rootUser, AbsSender sender, Chat chat) {
        var message = new SendDocument();
        message.setChatId(chat.getId().toString());
        scheduleService.getScheduleForUser(rootUser).ifPresentOrElse(schedule -> {
            message.setDocument(scheduleService.generateSchedule(schedule));
            execute(sender, message);
        }, () -> sendNoScheduleMessage(sender, chat));
    }

    private void sendNoScheduleMessage(AbsSender sender, Chat chat) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Расписание не найдено");
        execute(sender, message);
    }
}
