package by.mercury.vkontakte.facade.impl;

import by.mercury.core.data.MessageData;
import by.mercury.core.facade.MessageFacade;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Optional;

/**
 * Vkontakte implementation of {@link MessageFacade}
 *
 * @author Yegor Ikbaev
 */
@Slf4j
@Service
public class VkMessageFacade implements MessageFacade {

    private MessageService messageService;

    private Converter<MessageData, MessageModel> converter;

    @Override
    public void send(MessageData message) {
        Assert.notNull(message, "Message must not be null");
        Assert.hasText(message.getText(), "Text must not be null or empty");
        Assert.notNull(message.getTarget(), "Target user must not be null");

        var types = Optional.ofNullable(message.getTypes())
                .orElseGet(Collections::emptyList);
        log.debug("Send message {} to {} with types {}", message.getText(), message.getTarget(), types.size());
        
        messageService.send(converter.convert(message));
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setConverter(Converter<MessageData, MessageModel> converter) {
        this.converter = converter;
    }
}
