package by.mercury.telegram.command;

import by.mercury.core.model.UserModel;
import by.mercury.integration.service.SquadsterApiService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class TelegramDutyTimeCommand extends TelegramAbstractCommand {

    private static final String CURRENT_WEEK_MESSAGE = "You are duty on this week";
    private static final String MESSAGE_FORMAT = "You are duty through %d weeks";
    
    private SquadsterApiService squadsterApiService;
    
    public TelegramDutyTimeCommand(SquadsterApiService squadsterApiService) {
        super("duty", "Command to know duty time");
        this.squadsterApiService = squadsterApiService;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        getRootUser(user).ifPresentOrElse(rootUser -> sendDutyTime(rootUser, user, sender, chat),
                () -> sendNoSuchUserMessage(sender, chat));
    }

    private void sendDutyTime(UserModel rootUser, User telegramUser, AbsSender sender, Chat chat) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText(getDutyTimeText(rootUser));
        execute(sender, message);
    }
    
    private void sendNoSuchUserMessage(AbsSender sender, Chat chat) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("User is not valid");
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
