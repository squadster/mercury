package by.mercury.vkontakte.strategy.impl;

import by.mercury.core.data.MessageType;
import by.mercury.core.exception.SendMessageException;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.SpeechService;
import by.mercury.core.service.UserService;
import by.mercury.core.strategy.SendMessageStrategy;
import by.mercury.core.service.UploadService;
import com.vk.api.sdk.objects.enums.DocsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static java.nio.file.Files.deleteIfExists;

/**
 * Strategy for sending voice message
 *
 * @author Yegor Ikbaev
 */
public class VkVoiceSendMessageStrategy implements SendMessageStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkVoiceSendMessageStrategy.class);
    
    private SpeechService speechService;
    
    private UploadService uploadService;
    
    private UserService userService;
    
    @Override
    public boolean support(Collection<Channel> channels) {
        return Optional.ofNullable(channels).orElseGet(Collections::emptyList).contains(Channel.VK);
    }

    @Override
    public boolean support(MessageType messageType) {
        return messageType == MessageType.VOICE;
    }
    
    @Override
    public boolean support(UserModel user) {
        return Optional.ofNullable(user)
                .filter(u -> u.getPeerId() != null)
                .filter(u -> userService.getUserConfigurationForUser(u).getEnableVoiceMessages())
                .isPresent();
    }

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
    @Qualifier(value = "vkUploadService")
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
