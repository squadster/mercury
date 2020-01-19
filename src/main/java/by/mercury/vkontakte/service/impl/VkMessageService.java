package by.mercury.vkontakte.service.impl;

import by.mercury.core.exception.SendMessageException;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class VkMessageService implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkMessageService.class);
    private static final Random RANDOM = new SecureRandom();

    private VkApiClient vkApiClient;

    private GroupActor vkGroupActor;

    @Override
    public void send(MessageModel message) {
        try {
            vkApiClient.messages().send(vkGroupActor)
                    .peerId(message.getTarget().getPeerId())
                    .message(message.getText())
                    .randomId(RANDOM.nextInt())
                    .execute();
        } catch (ApiException | ClientException exception) {
            LOGGER.warn(exception.getMessage());
            throw new SendMessageException(exception.getMessage(), exception);
        }
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
