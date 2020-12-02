package by.mercury.core.facade.impl;

import by.mercury.core.data.MessageData;
import by.mercury.core.facade.MessageFacade;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import by.mercury.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Optional;

/**
 * Implementation of {@link MessageFacade}
 *
 * @author Yegor Ikbaev
 */
@Slf4j
@Service
public class MessageFacadeImpl implements MessageFacade {

    private final MessageService messageService;

    private final UserService userService;
    
    private final Converter<MessageData, MessageModel> converter;

    public MessageFacadeImpl(MessageService messageService, UserService userService, 
                             Converter<MessageData, MessageModel> converter) {
        this.messageService = messageService;
        this.userService = userService;
        this.converter = converter;
    }

    @Override
    public void send(MessageData message) {
        validate(message);

        var types = Optional.ofNullable(message.getTypes())
                .orElseGet(Collections::emptyList);
        log.debug("Send message {} to {} with types {}", message.getText(), message.getTarget(), types.size());
        
        messageService.send(converter.convert(message));
    }

    @Override
    public void notify(MessageData message) {
        validate(message);

        var types = Optional.ofNullable(message.getTypes())
                .orElseGet(Collections::emptyList);
        log.debug("Send message {} to {} with types {}", message.getText(), message.getTarget(), types.size());
        var messageModel = converter.convert(message);
        var channels = userService.getAvailableChannels(messageModel.getTarget(), messageModel.getTargetChannels());
        messageModel.setTargetChannels(channels);
        
        messageService.send(messageModel);
    }

    private void validate(MessageData message) {
        Assert.notNull(message, "Message must not be null");
        Assert.hasText(message.getText(), "Text must not be null or empty");
        Assert.notNull(message.getTarget(), "Target user must not be null");
    }
}
