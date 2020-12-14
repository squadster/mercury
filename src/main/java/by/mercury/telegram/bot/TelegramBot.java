package by.mercury.telegram.bot;

import by.mercury.core.model.UserModel;
import by.mercury.core.service.ScheduleService;
import by.mercury.core.service.SpeechService;
import by.mercury.core.service.UserService;
import by.mercury.telegram.command.StartCommand;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class TelegramBot extends TelegramLongPollingCommandBot {

    private String name = System.getProperty("telegram.access.name");

    private String token = System.getProperty("telegram.access.token");

    private UserService userService;

    @Autowired
    private SpeechService speechService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StartCommand startCommand;

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

    @SneakyThrows
    @Override
    public void processNonCommandUpdate(Update update) {
        if (!update.hasMessage()) {
            throw new IllegalStateException("Update doesn't have a body!");
        }
        if (update.getMessage().getVoice() != null) {
            var getFile = new GetFile().setFileId(update.getMessage().getVoice().getFileId());
            var filePath = execute(getFile).getFilePath();
            var file = downloadFile(filePath, new File(UUID.randomUUID().toString() + ".ogg"));
            var text = speechService.recognize(file);
            if (sendIfSchedule(text.toLowerCase(), update.getMessage())) {
                return;
            }
            var answer = new SendMessage();
            answer.setText("Не удалось распознать сообщение: " + text);
            answer.setChatId(update.getMessage().getChatId());
            execute(answer);
            return;
        }
        var message = update.getMessage();
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

    private boolean sendIfSchedule(String text, Message message) {
        if (text.contains("timetable") || text.contains("scehdule"))
        startCommand.getRootUser(message.getFrom()).ifPresentOrElse(rootUser -> sendSchedule(rootUser, message.getChat()),
                () -> sendNoScheduleMessage(message.getChat()));
        return text.contains("timetable") || text.contains("scehdule");
    }

    @SneakyThrows
    private void sendSchedule(UserModel rootUser, Chat chat) {
        var message = new SendDocument();
        message.setChatId(chat.getId().toString());
        Optional.of(scheduleService.getScheduleForUser(rootUser)).ifPresentOrElse(schedule -> {
            message.setDocument(scheduleService.generateSchedule(schedule));
            try {
                execute(message);
            } catch (TelegramApiException exception) {
                exception.printStackTrace();
            }
        }, () -> sendNoScheduleMessage(chat));
    }

    @SneakyThrows
    private void sendNoScheduleMessage(Chat chat) {
        var message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.setText("Расписание не найдено");
        execute(message);
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
