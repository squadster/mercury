package by.mercury.vkontakte.preprocessor;

import by.mercury.core.command.CommandContext;
import by.mercury.core.command.CommandPreprocessor;
import com.vk.api.sdk.objects.messages.AudioMessage;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.messages.MessageAttachmentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Optional;

import static by.mercury.vkontakte.service.impl.VkCommandContextService.VK_MESSAGE;

@Slf4j
@Component
public class AudioMessagePreprocessor implements CommandPreprocessor {

    public static final Integer PRIORITY = 11;
    
    public static final String AUDIO_MESSAGE_KEY = "audioMessage";

    private static final int BUFFER_SIZE = 8 * 1024;
    private static final int END_DOWNLOADING_POSITION = -1;
    private static final String MP3_EXTENSION = ".mp3";
    private static final String FILE_PREFIX_FORMAT = "id_%d_";
    
    @Override
    public void preprocess(CommandContext context) {
        Optional.ofNullable(context.getParameters().get(VK_MESSAGE))
                .filter(message -> message instanceof Message)
                .map(message -> (Message) message)
                .stream()
                .map(Message::getAttachments)
                .flatMap(Collection::stream)
                .filter(messageAttachment -> MessageAttachmentType.AUDIO_MESSAGE == messageAttachment.getType())
                .map(MessageAttachment::getAudioMessage)
                .map(this::download)
                .findFirst()
                .ifPresent(audio -> context.getParameters().put(AUDIO_MESSAGE_KEY, audio));
    }

    private File download(AudioMessage message) {
        var file = createFile(message);
        try (var myUrlStream = message.getLinkMp3().openStream();
             var myUrlChannel = Channels.newChannel(myUrlStream);
             var destinationChannel = new FileOutputStream(file).getChannel()) {

            var buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (myUrlChannel.read(buffer) != END_DOWNLOADING_POSITION) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    destinationChannel.write(buffer);
                }
                buffer.clear();
            }
            log.info("Audio message is downloaded  with id: {}, ownerId: {}", message.getId(), message.getOwnerId());
            return file;
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private File createFile(AudioMessage audio) {
        try {
            return Files.createTempFile(String.format(FILE_PREFIX_FORMAT, audio.getId()), MP3_EXTENSION).toFile();
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Override
    public Integer getPriority() {
        return PRIORITY;
    }
}
