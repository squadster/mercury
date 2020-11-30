package by.mercury.telegram.command;

import by.mercury.core.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class SetTokenCommand extends TelegramAbstractCommand {

    public SetTokenCommand() {
        super("set_token", "Установить токен");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        var token = getArgument(arguments, 0);
        getRootUser(token).ifPresentOrElse(rootUser -> updateUser(rootUser, user, sender, chat), 
                () -> sendNotValidTokenMessage(sender, chat));
        log.info("SetToken command for TELEGRAM was successfully completed");
    }
    
    private void updateUser(UserModel rootUser, User telegramUser, AbsSender sender, Chat chat) {
        rootUser.setTelegramId(telegramUser.getId());
        getUserService().save(rootUser);
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Токен верный, теперь вы можете работать с ботом");
        execute(sender, message);
    }
    
    private void sendNotValidTokenMessage(AbsSender sender, Chat chat) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Токен неверный, попробуйте еще раз");
        execute(sender, message);
    }
}
