package by.mercury.vkontakte.command;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Vk test implementation of {@link Command}
 *
 * @author Yegor Ikbaev
 */
@Slf4j
@Component
public class VkUnknownCommand extends AbstractVkCommand {

    public VkUnknownCommand() {
        super(Collections.emptyList());
    }

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
                .types(Collections.singletonList(MessageType.TEXT))
                .targetChannels(Collections.singleton(Channel.VK))
                .build();
        getMessageService().send(message);
        log.info("Unknown command for VK was received");
    }
}
