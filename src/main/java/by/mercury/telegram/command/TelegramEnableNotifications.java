package by.mercury.telegram.command;

import by.mercury.core.model.UserModel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class TelegramEnableNotifications extends TelegramAbstractCommand {

    public TelegramEnableNotifications() {
        super("enable", "Включение уведомлений");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
        getRootUser(user).ifPresentOrElse(rootUser -> enable(rootUser, sender, chat), () -> sendNoSuchUser(sender, chat));
    }

    private void enable(UserModel rootUser, AbsSender sender, Chat chat) {
        getUserService().updateNotificationsSettings(rootUser,  settings -> settings.setEnableNotificationsTelegram(true));
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Уведомления включены");
        execute(sender, message);
    }

    private void sendNoSuchUser(AbsSender sender, Chat chat) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Вы не зарегистрированы");
        execute(sender, message);
    }
}
