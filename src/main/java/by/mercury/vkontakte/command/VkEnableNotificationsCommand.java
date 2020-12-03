package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class VkEnableNotificationsCommand extends AbstractVkCommand {

    public VkEnableNotificationsCommand() {
        super(Arrays.asList("включить", "enable"));
    }

    @Override
    public void execute(CommandContext context) {
        var author = context.getMessage().getAuthor();
        author.setEnableNotificationsVk(true);
        getUserService().save(author);
        var message = MessageModel.builder()
                .text("Уведомления включены")
                .target(context.getMessage().getAuthor())
                .types(Collections.singletonList(MessageType.TEXT))
                .targetChannels(Collections.singleton(Channel.VK))
                .build();
        getMessageService().send(message);
    }
}

