package by.mercury.telegram.command;

import by.mercury.core.model.UserModel;
import by.mercury.integration.service.SquadsterApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class TelegramDutyTimeCommand extends TelegramAbstractCommand {

    private static final String CURRENT_WEEK_MESSAGE = "Вы дежурный на этой недели";
    private static final String MESSAGE_FORMAT = "Вы дежурный через %d недель";
    
    private SquadsterApiService squadsterApiService;
    
    public TelegramDutyTimeCommand(SquadsterApiService squadsterApiService) {
        super("duty", "Узнать, когда вы дежурный");
        this.squadsterApiService = squadsterApiService;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        getRootUser(user).ifPresentOrElse(rootUser -> sendDutyTime(rootUser, sender, chat),
                () -> sendNoSuchUserMessage(sender, chat));
        log.info("DutyTime command for TELEGRAM was successfully completed");
    }

    private void sendDutyTime(UserModel rootUser, AbsSender sender, Chat chat) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText(getDutyTimeText(rootUser));
        execute(sender, message);
    }
    
    private void sendNoSuchUserMessage(AbsSender sender, Chat chat) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Вы не зарегистрировались");
        execute(sender, message);
    } 

    private String getDutyTimeText(UserModel user) {
        var queue = squadsterApiService.getQueueData(user).getQueueNumber();
        var weeks = queue - 1;
        if  (weeks == 0) {
            return CURRENT_WEEK_MESSAGE;
        }
        return String.format(MESSAGE_FORMAT, weeks);
    }
}
