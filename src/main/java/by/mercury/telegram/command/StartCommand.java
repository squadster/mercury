package by.mercury.telegram.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class StartCommand extends TelegramAbstractCommand {

    public StartCommand() {
        super("start", "Command for start");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        var builder = new StringBuilder();
        if (getRootUser(user).isEmpty()) {
            builder.append("Hi, ").append(user.getUserName())
                    .append("! You've been added to bot users' list!\n")
                    .append("Please execute command:\n'/set_token <token>'\nwhere &lt;token&gt; is the token you got in VK");
        } else {
            builder.append("You've already started bot!");
        }
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText(builder.toString());
        execute(sender, message);
    }
}
