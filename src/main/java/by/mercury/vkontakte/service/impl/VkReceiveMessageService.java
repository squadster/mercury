package by.mercury.vkontakte.service.impl;

import by.mercury.core.service.MessageReceiveService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Vkontakte implementation of {@link MessageReceiveService}
 *
 * @author Yegor Ikbaev
 */
@Service
public class VkReceiveMessageService implements MessageReceiveService<Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkReceiveMessageService.class);
    private static final Integer INITIAL_MAX_MESSAGE_ID = -1;
    private static final Integer INITIAL_TIMESTAMP = -1;

    private VkApiClient vkApiClient;

    private GroupActor vkGroupActor;

    private AtomicInteger maxMessageId = new AtomicInteger(INITIAL_MAX_MESSAGE_ID);

    private AtomicInteger timestamp = new AtomicInteger(INITIAL_TIMESTAMP);

    @PostConstruct
    private void updateTimestamp() throws ClientException, ApiException {
        Integer timeStampValue = vkApiClient.messages()
                .getLongPollServer(vkGroupActor)
                .execute()
                .getTs();
        timestamp.accumulateAndGet(timeStampValue, (arg1, arg2) -> timeStampValue);
    }

    @Override
    public List<Message> receiveAll() {
        List<Message> messages = Collections.emptyList();
        try {
            messages = getLongPollHistoryQuery().execute()
                    .getMessages()
                    .getItems().stream()
                    .filter(Predicate.not(this::isOwnMessage))
                    .collect(Collectors.toList());
            updateTimestamp();
            findMaxMessageId(messages).ifPresent(newValue -> maxMessageId.accumulateAndGet(newValue, Math::max));
        } catch (ApiException | ClientException exception) {
            LOGGER.warn(exception.getMessage());
        }
        return messages;
    }

    private MessagesGetLongPollHistoryQuery getLongPollHistoryQuery() {
        MessagesGetLongPollHistoryQuery query = vkApiClient.messages()
                .getLongPollHistory(vkGroupActor)
                .ts(timestamp.get());
        if (maxMessageId.get() > INITIAL_MAX_MESSAGE_ID) {
            query = query.maxMsgId(maxMessageId.get());
        }
        return query;
    }

    private boolean isOwnMessage(Message message) {
        return message.getFromId().equals(Math.negateExact(vkGroupActor.getGroupId()));
    }

    private Optional<Integer> findMaxMessageId(List<Message> messages) {
        return messages.stream()
                .filter(Predicate.not(Message::isOut))
                .map(Message::getId)
                .max(Integer::compareTo);
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
