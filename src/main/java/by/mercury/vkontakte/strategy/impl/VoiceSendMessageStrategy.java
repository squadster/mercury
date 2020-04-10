package by.mercury.vkontakte.strategy.impl;

import by.mercury.core.exception.SendMessageException;
import by.mercury.core.model.MessageModel;
import by.mercury.core.strategy.SendMessageStrategy;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.enums.DocsType;
import marytts.LocalMaryInterface;
import marytts.exceptions.SynthesisException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

import static java.io.File.createTempFile;
import static java.lang.String.format;
import static java.nio.file.Files.deleteIfExists;
import static marytts.util.data.audio.MaryAudioUtils.getSamplesAsDoubleArray;
import static marytts.util.data.audio.MaryAudioUtils.writeWavFile;

/**
 * Strategy for sending voice message
 *
 * @author Yegor Ikbaev
 */
public class VoiceSendMessageStrategy implements SendMessageStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceSendMessageStrategy.class);
    private static final Random RANDOM = new SecureRandom();
    private static final String VOICE_NAME = "cmu-slt-hsmm";
    private static final String AUDIO_MESSAGE_FORMAT = "doc%d_%d";
    private static final String AUDIO_MESSAGE_FILE_EXTENSION = ".wav";
    private static final String AUDIO_MESSAGE_PREFIX = "id_%d_";

    private LocalMaryInterface localMaryInterface;

    private VkApiClient vkApiClient;

    private GroupActor vkGroupActor;
    
    @Override
    public void send(MessageModel message) {
        try {
            var audioMessage = synthesizeVoice(message);
            LOGGER.debug("Created message: text = {}, file={}", message.getText(), audioMessage.getName());
            sendMessage(message, uploadFile(message, audioMessage));
            LOGGER.debug("Is file {} deleted:{}", audioMessage.getName(), deleteIfExists(audioMessage.toPath()));
        } catch (SynthesisException | IOException | ClientException | ApiException exception) {
            LOGGER.debug("Error during sending message {} to {}", message.getText(), message.getTarget().getPeerId());
            throw new SendMessageException(exception.getMessage(), exception);
        }
    }

    private File synthesizeVoice(MessageModel message) throws SynthesisException, IOException {
        localMaryInterface.setVoice(VOICE_NAME);
        var audio = localMaryInterface.generateAudio(message.getText());
        double[] binary = getSamplesAsDoubleArray(audio);
        var filePrefix = format(AUDIO_MESSAGE_PREFIX, message.getTarget().getPeerId());
        var tempFile = createTempFile(filePrefix, AUDIO_MESSAGE_FILE_EXTENSION);
        writeWavFile(binary, tempFile.getAbsolutePath(), audio.getFormat());
        return tempFile;
    }
    
    private String uploadFile(MessageModel message, File voiceMessage) throws ClientException, ApiException {
        var url = vkApiClient.upload()
                .doc(getServerUrl(message), voiceMessage)
                .execute()
                .getFile();
        var audioMessage = vkApiClient
                .docs()
                .save(vkGroupActor, url)
                .execute()
                .getAudioMessage();
        return format(AUDIO_MESSAGE_FORMAT, audioMessage.getOwnerId(), audioMessage.getId());
    }

    private void sendMessage(MessageModel message, String url) throws ClientException, ApiException {
        vkApiClient.messages().send(vkGroupActor)
                .peerId(message.getTarget().getPeerId())
                .attachment(url)
                .message(StringUtils.EMPTY)
                .randomId(RANDOM.nextInt())
                .execute();
    }

    private String getServerUrl(MessageModel message) throws ClientException, ApiException {
        return vkApiClient.docs()
                .getMessagesUploadServer(vkGroupActor)
                .peerId(message.getTarget().getPeerId())
                .type(DocsType.AUDIO_MESSAGE)
                .execute()
                .getUploadUrl()
                .toString();
    }

    @Autowired
    public void setLocalMaryInterface(LocalMaryInterface localMaryInterface) {
        this.localMaryInterface = localMaryInterface;
    }

    @Autowired
    public void setVkApiClient(VkApiClient vkApiClient) {
        this.vkApiClient = vkApiClient;
    }

    @Autowired
    public void setVkGroupActor(GroupActor vkGroupActor) {
        this.vkGroupActor = vkGroupActor;
    }
}
