package by.mercury.core.service.impl;

import by.mercury.core.data.MessageType;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import by.mercury.core.strategy.SendMessageStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DefaultMessageService implements MessageService {

    private List<SendMessageStrategy> sendMessageStrategies;

    public DefaultMessageService(List<SendMessageStrategy> sendMessageStrategies) {
        this.sendMessageStrategies = new ArrayList<>(sendMessageStrategies);
    }

    @Override
    public void send(MessageModel message) {
        Optional.ofNullable(message.getTypes())
                .filter(Predicate.not(Collection::isEmpty))
                .orElseGet(() -> Collections.singletonList(MessageType.TEXT))
                .stream()
                .map(this::findStrategiesForType)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(strategy -> strategy.support(message.getTargetChannels()))
                .filter(strategy -> strategy.support(message.getTarget()))
                .forEach(strategy -> strategy.send(message));
    }

    private List<SendMessageStrategy> findStrategiesForType(MessageType type) {
        return sendMessageStrategies.stream()
                .filter(strategy -> strategy.support(type))
                .collect(Collectors.toList());
    }

    public void setSendMessageStrategies(List<SendMessageStrategy> sendMessageStrategies) {
        this.sendMessageStrategies = new ArrayList<>(sendMessageStrategies);
    }
}
