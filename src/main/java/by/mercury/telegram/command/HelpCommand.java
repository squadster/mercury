package by.mercury.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public class HelpCommand extends TelegramAbstractCommand {

    private List<TelegramAbstractCommand> botCommands;

    public HelpCommand() {
        super("help", "Узнать все команды");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        var builder = new StringBuilder("<b>Доступные команды:</b>");
        botCommands.forEach(botCommand -> builder.append(botCommand.toString()).append("\n"));
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.enableHtml(true);
        message.setText(builder.toString());
        execute(sender, message);
    }

    @Autowired
    public void setBotCommands(List<TelegramAbstractCommand> botCommands) {
        this.botCommands = botCommands;
    }
}
