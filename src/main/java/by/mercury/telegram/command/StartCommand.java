package by.mercury.telegram.command;

import by.mercury.core.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class StartCommand extends TelegramAbstractCommand {

    private final UserService userService;

    public StartCommand(UserService userService) {
        super("start", "Начать беседу");
        this.userService = userService;
    }

    @Override
    @Transactional
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        var builder = new StringBuilder();
        if (getRootUser(user).isEmpty()) {
            builder.append("Привет, ").append(user.getUserName())
                    .append("Выполни команду:\n'/set_token <token>'\nгде &lt;token&gt; - токен, который вы получили из VK");
            persistChatIdForUser(user, chat);
        } else {
            builder.append("Вы уже начали беседу");
        }
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText(builder.toString());
        execute(sender, message);
    }

    public void persistChatIdForUser(User user, Chat chat) {
        userService.findByTelegramId(user.getId())
                .filter(retrievedUser -> retrievedUser.getChatId() == null)
                .ifPresent(retrievedUser -> {
                    retrievedUser.setChatId(chat.getId());
                    userService.save(retrievedUser);
                });

    }
}
