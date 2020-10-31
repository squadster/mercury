package by.mercury.vkontakte.service.impl;

import by.mercury.core.model.MessageModel;
import by.mercury.vkontakte.service.UploadService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.docs.responses.SaveResponse;
import com.vk.api.sdk.objects.enums.DocsType;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import static java.lang.String.format;

@Service
public class VkUploadService implements UploadService {

    private static final Random RANDOM = new SecureRandom();
    private static final String MESSAGE_FORMAT = "doc%d_%d";
    private Map<DocsType, Function<SaveResponse, String>> extractors;
    
    private VkApiClient vkApiClient;

    private GroupActor vkGroupActor;

    public VkUploadService(VkApiClient vkApiClient, GroupActor vkGroupActor) {
        this.vkApiClient = vkApiClient;
        this.vkGroupActor = vkGroupActor;
        extractors = Map.of(DocsType.AUDIO_MESSAGE, response -> {
            var doc = response.getAudioMessage();
            return format(MESSAGE_FORMAT, doc.getOwnerId(), doc.getId());
        }, DocsType.DOC, response -> {
            var doc = response.getDoc();
            return format(MESSAGE_FORMAT, doc.getOwnerId(), doc.getId());
        });
    }

    @Override
    public String uploadFile(MessageModel message, File file, DocsType type) {
        try {
            var url = vkApiClient.upload()
                    .doc(getServerUrl(message, type), file)
                    .execute()
                    .getFile();
            var response = vkApiClient
                    .docs()
                    .save(vkGroupActor, url)
                    .execute();
            var docUrl = extractors.get(type).apply(response);
            return sendFile(message, docUrl);
        } catch (ApiException | ClientException exception) {
            throw new IllegalStateException(exception);
        }
    }
    
    private String getServerUrl(MessageModel message, DocsType type) throws ClientException, ApiException {
        return vkApiClient.docs()
                .getMessagesUploadServer(vkGroupActor)
                .peerId(message.getTarget().getPeerId())
                .type(type)
                .execute()
                .getUploadUrl()
                .toString();
    }

    private String sendFile(MessageModel message, String url) throws ClientException, ApiException {
        vkApiClient.messages().send(vkGroupActor)
                .peerId(message.getTarget().getPeerId())
                .attachment(url)
                .message(StringUtils.EMPTY)
                .randomId(RANDOM.nextInt())
                .execute();
        return url;
    }
}
