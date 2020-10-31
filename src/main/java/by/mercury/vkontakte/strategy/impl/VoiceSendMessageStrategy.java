package by.mercury.vkontakte.strategy.impl;

import by.mercury.core.exception.SendMessageException;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.SpeechService;
import by.mercury.core.strategy.SendMessageStrategy;
import by.mercury.vkontakte.service.UploadService;
import com.vk.api.sdk.objects.enums.DocsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UncheckedIOException;

import static java.nio.file.Files.deleteIfExists;

/**
 * Strategy for sending voice message
 *
 * @author Yegor Ikbaev
 */
public class VoiceSendMessageStrategy implements SendMessageStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceSendMessageStrategy.class);
    
    private SpeechService speechService;
    
    private UploadService uploadService;

    @Override
    public void send(MessageModel message) {
        try {
            var audioMessage = speechService.synthesize(message);
            LOGGER.debug("Created message: text = {}, file={}", message.getText(), audioMessage.getName());
            uploadService.uploadFile(message, audioMessage, DocsType.AUDIO_MESSAGE);
            LOGGER.debug("Is file {} deleted:{}", audioMessage.getName(), deleteIfExists(audioMessage.toPath()));
        } catch (IOException | UncheckedIOException | IllegalArgumentException | IllegalStateException exception) {
            LOGGER.debug("Error during sending message {} to {}", message.getText(), message.getTarget().getPeerId());
            throw new SendMessageException(exception.getMessage(), exception);
        }
    }

    @Autowired
    public void setSpeechService(SpeechService speechService) {
        this.speechService = speechService;
    }

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }
}
