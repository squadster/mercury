package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class VkEchoCommand extends AbstractVkCommand {

    public VkEchoCommand() {
        super(Arrays.asList("echo"));
    }

    @Override
    public void execute(CommandContext context) {
        var text = context.getMessage().getText().replaceFirst("echo", "").strip();
        
        var message = MessageModel.builder()
                .text(text)
                .target(context.getMessage().getAuthor())
                .types(Arrays.asList(MessageType.TEXT, MessageType.VOICE))
                .targetChannels(Collections.singleton(Channel.VK))
                .build();
        getMessageService().send(message);    
    }
}
