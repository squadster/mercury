package by.mercury.vkontakte.preprocessor;

import by.mercury.core.command.CommandContext;
import by.mercury.core.command.CommandPreprocessor;
import by.mercury.core.service.SpeechService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static by.mercury.vkontakte.preprocessor.AudioMessagePreprocessor.AUDIO_MESSAGE_KEY;

@Slf4j
@Component
public class AudioMessageRecognizePreprocessor implements CommandPreprocessor {

    public static final Integer PRIORITY = 11;

    private SpeechService speechService;

    @Override
    public void preprocess(CommandContext context) {
        Optional.ofNullable(context.getParameters().get(AUDIO_MESSAGE_KEY))
                .filter(message -> message instanceof File)
                .map(message -> (File) message)
                .map(this::recognize)
                .ifPresentOrElse(text -> updateMessage(context, text), () -> clearAudioFile(context));
    }

    private String recognize(File audio) {
        try {
            return speechService.recognize(audio);
        } catch (IllegalArgumentException exception) {
            log.warn("Error during message recognizing: {}", exception.getMessage());
        }
        return null;
    }

    private void updateMessage(CommandContext context, String text) {
        context.getMessage().setText(text);
        clearAudioFile(context);
    }

    private void clearAudioFile(CommandContext context) {
        Optional.ofNullable(context.getParameters().get(AUDIO_MESSAGE_KEY))
                .filter(message -> message instanceof File)
                .map(message -> (File) message)
                .ifPresent(file -> {
                    try {
                        Files.deleteIfExists(file.toPath());
                    } catch (IOException exception) {
                        log.warn("Error during deleting audio message: {}", exception.getMessage());
                    }
                });
    }

    @Override
    public Integer getPriority() {
        return PRIORITY;
    }

    @Autowired
    public void setSpeechService(SpeechService speechService) {
        this.speechService = speechService;
    }
}
