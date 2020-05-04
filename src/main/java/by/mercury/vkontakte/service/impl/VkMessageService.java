package by.mercury.vkontakte.service.impl;

import by.mercury.core.data.MessageType;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import by.mercury.core.strategy.SendMessageStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class VkMessageService implements MessageService {

    private Map<MessageType, SendMessageStrategy> sendMessageStrategies;

    @Override
    public void send(MessageModel message) {
        Optional.ofNullable(message.getTypes())
                .filter(Predicate.not(Collection::isEmpty))
                .orElseGet(() -> Collections.singletonList(MessageType.TEXT))
                .stream()
                .map(sendMessageStrategies::get)
                .forEach(strategy -> strategy.send(message));
    }

    @Autowired
    public void setSendMessageStrategies(Map<MessageType, SendMessageStrategy> sendMessageStrategies) {
        this.sendMessageStrategies = sendMessageStrategies;
    }
}
