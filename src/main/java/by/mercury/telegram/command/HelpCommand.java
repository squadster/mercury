package by.mercury.telegram.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class HelpCommand extends TelegramAbstractCommand {

    private ICommandRegistry commandRegistry;
    
    public HelpCommand() {
        super("help", "Узнать все команды");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        var builder = new StringBuilder("<b>Доступные команды:</b>");
        commandRegistry.getRegisteredCommands().forEach(cmd -> builder.append(cmd.toString()).append("\n"));
        
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.enableHtml(true);
        message.setText(builder.toString());
        execute(sender, message);
    }

    @Autowired
    public void setCommandRegistry(ICommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }
}
