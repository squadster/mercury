package by.mercury.telegram.bot;

import by.mercury.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

@Slf4j
public class TelegramBot extends TelegramLongPollingCommandBot {

    private String name = System.getProperty("telegram.access.name");

    private String token = System.getProperty("telegram.access.token");

    private UserService userService;

    private List<BotCommand> commands;

    public TelegramBot(DefaultBotOptions botOptions, List<BotCommand> commands, UserService userService) {
        super(botOptions, true);
        this.userService = userService;
        this.commands = commands;
        this.commands.forEach(this::register);
        registerDefaultAction(((sender, message) -> {
            var sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText(message.getText() + " command not found!");
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException exception) {
                log.warn("Error during sending {} : {}", message, exception.getMessage());
            }
        }));
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (!update.hasMessage()) {
            throw new IllegalStateException("Update doesn't have a body!");
        }

        var message = update.getMessage();
        /*GetFile getFile = new GetFile().setFileId(update.getMessage().getVoice().getFileId());
        String filePath = execute(getFile).getFilePath();
        File file = downloadFile(filePath, outputFile);*/
        var user = message.getFrom();
        try {
            if (!canSendMessage(user, message)) {
                return;
            }
            var clearMessage = message.getText();
            var answer = new SendMessage();
            answer.setText(clearMessage);
            answer.setChatId(message.getChatId());
            execute(answer);
        } catch (TelegramApiException exception) {
            log.warn("Error: {}", exception);
        }
    }

    private boolean canSendMessage(User user, Message msg) throws TelegramApiException {
        var message = new SendMessage();
        message.setChatId(msg.getChatId());
        if (!msg.hasText() || msg.getText().trim().length() == 0) {
            message.setText("You shouldn't send empty messages!");
            execute(message);
            return false;
        }

        if (userService.findByTelegramId(user.getId()).isEmpty()) {
            message.setText("Firstly you should start bot! Use /start and /set_token commands!");
            execute(message);
            return false;
        }
        return true;
    }
}
