package by.mercury.vkontakte.strategy.impl;

import by.mercury.core.exception.SendMessageException;
import by.mercury.core.model.MessageModel;
import by.mercury.core.strategy.SendMessageStrategy;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Strategy for sending text message
 *
 * @author Yegor Ikbaev
 */
public class TextSendMessageStrategy implements SendMessageStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextSendMessageStrategy.class);
    private static final Random RANDOM = new SecureRandom();

    private VkApiClient vkApiClient;

    private GroupActor vkGroupActor;

    /**
     * Send text message.
     * 
     * @param message must not be {@literal null}.
     */
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
