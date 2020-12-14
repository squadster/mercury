package by.mercury.telegram.command;

import by.mercury.core.model.MessageModel;
import by.mercury.core.service.SpeechService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TelegramEchoCommand extends TelegramAbstractCommand {

    private final SpeechService speechService;

    public TelegramEchoCommand(SpeechService speechService) {
        super("echo", "озвучить текст");
        this.speechService = speechService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        var voiceId = getArgument(arguments, 0).toUpperCase();
        var text = getText(arguments);
        try {
            var audioMessage = new SendAudio();
            audioMessage.setAudio(speechService.synthesize(text, voiceId));
            audioMessage.setChatId(chat.getId().toString());
            absSender.execute(audioMessage);
            var textMessage = new SendMessage();
            textMessage.setText(text);
            textMessage.setChatId(chat.getId().toString());
            absSender.execute(textMessage);
        } catch (TelegramApiException exception) {
            log.warn("Exception during sending message: ", exception);
        }
    }

    private String getText(String[] arguments) {
        return Arrays.stream(arguments)
                .skip(1L)
                .collect(Collectors.joining(" "));
    }
}
