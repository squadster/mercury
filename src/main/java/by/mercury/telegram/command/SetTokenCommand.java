package by.mercury.telegram.command;

import by.mercury.core.model.UserModel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class SetTokenCommand extends TelegramAbstractCommand {

    public SetTokenCommand() {
        super("set_token", "Command for setting token");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        var token = getArgument(arguments, 0);
        getRootUser(token).ifPresentOrElse(rootUser -> updateUser(rootUser, user, sender, chat), 
                () -> sendNotValidTokenMessage(user, sender, chat));
        
    }
    
    private void updateUser(UserModel rootUser, User telegramUser, AbsSender sender, Chat chat) {
        rootUser.setTelegramId(telegramUser.getId());
        getUserService().save(rootUser);
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Token is valid");
        execute(sender, message);
    }
    
    private void sendNotValidTokenMessage(User telegramUser, AbsSender sender, Chat chat) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Token is invalid, try again");
        execute(sender, message);
    }
}
