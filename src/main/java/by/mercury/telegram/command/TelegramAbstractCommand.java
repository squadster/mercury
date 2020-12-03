package by.mercury.telegram.command;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import by.mercury.core.model.Channel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Slf4j
public abstract class TelegramAbstractCommand extends BotCommand implements Command {

    private UserService userService;
    
    public TelegramAbstractCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public boolean support(Channel channel) {
        return channel == Channel.TELEGRAM;
    }

    @Override
    public boolean support(CommandContext context) {
        return true;
    }

    @Override
    public void execute(CommandContext context) {
        //do nothing
    }
    
    protected void execute(AbsSender sender, SendMessage message) {
        try {
            sender.execute(message);
            log.info("Sent message {}", message);
        } catch (TelegramApiException exception) {
            log.warn("Error during sending {} : {}", message, exception.getMessage());
        }
    }

    protected void execute(AbsSender sender, SendDocument document) {
        try {
            log.info("Sent document {}", document);
            sender.execute(document);
        } catch (TelegramApiException exception) {
            log.warn("Error during sending {} : {}", document, exception.getMessage());
        }
    }
    
    protected Optional<UserModel> getRootUser(User user) {
        return Optional.of(user)
                .map(User::getId)
                .flatMap(userService::findByTelegramId);
    }

    protected Optional<UserModel> getRootUser(String token) {
        return Optional.ofNullable(token).flatMap(userService::findByTelegramToken);
    }
    
    protected String getArgument(String[] arguments, int index) {
        return Optional.ofNullable(arguments)
                .filter(args -> args.length > index)
                .map(args -> args[index])
                .orElse(StringUtils.EMPTY);
    }

    protected UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
