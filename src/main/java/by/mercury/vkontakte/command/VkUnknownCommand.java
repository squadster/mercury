package by.mercury.vkontakte.command;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Vk test implementation of {@link Command}
 *
 * @author Yegor Ikbaev
 */
@Slf4j
@Component
public class VkUnknownCommand implements Command {
    
    private MessageService messageService;

    @Override
    public boolean support(CommandContext context) {
        return false;
    }

    @Override
    public void execute(CommandContext context) {
        var source = context.getMessage();
        log.info("Did not recognize message: {}, from {}", source.getText(), source.getAuthor().getPeerId());
        var message = MessageModel.builder()
                .target(source.getAuthor())
                .text("Не удалось распознать сообщение")
                .types(Collections.singletonList(MessageType.VOICE))
                .build();
        messageService.send(message);
    }
    
    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
