package by.mercury.core.service.impl;

import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import by.mercury.core.strategy.SendMessageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DefaultMessageService implements MessageService {

    private static final String AND = " and ";

    private List<SendMessageStrategy> sendMessageStrategies;

    @Override
    public void send(MessageModel message) {
        log.info("New {} message will be sent to {} for user {}",
                message.getTypes().stream()
                        .map(MessageType::getCode)
                        .collect(Collectors.joining(AND)),
                message.getTargetChannels().stream()
                        .map(Channel::name)
                        .collect(Collectors.joining(AND)),
                message.getTarget().getId());
        Optional.ofNullable(message.getTypes())
                .filter(Predicate.not(Collection::isEmpty))
                .orElseGet(() -> Collections.singletonList(MessageType.TEXT))
                .stream()
                .map(this::findStrategiesForType)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(strategy -> strategy.support(message.getTargetChannels()))
                .forEach(strategy -> strategy.send(message));
    }

    private List<SendMessageStrategy> findStrategiesForType(MessageType type) {
        return sendMessageStrategies.stream()
                .filter(strategy -> strategy.support(type))
                .collect(Collectors.toList());
    }

    @Autowired
    public void setSendMessageStrategies(List<SendMessageStrategy> sendMessageStrategies) {
        this.sendMessageStrategies = sendMessageStrategies;
    }
}
