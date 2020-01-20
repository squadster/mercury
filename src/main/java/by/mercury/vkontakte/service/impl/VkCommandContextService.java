package by.mercury.vkontakte.service.impl;

import by.mercury.core.command.CommandContext;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.CommandContextService;
import by.mercury.core.service.UserService;
import by.mercury.vkontakte.command.VkCommandContext;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Vkontakte implementation of {@link CommandContextService}
 *
 * @author Yegor Ikbaev
 */
@Service
public class VkCommandContextService implements CommandContextService<Message> {

    private UserService userService;

    @Override
    public CommandContext build(Message message) {
        Assert.notNull(message, "Message must not be null");

        return VkCommandContext.builder()
                .message(createMessageModel(message))
                .build();
    }

    private MessageModel createMessageModel(Message message) {
        return MessageModel.builder()
                .text(message.getText())
                .author(gerOrCreateAuthor(message))
                .build();
    }

    private UserModel gerOrCreateAuthor(Message message) {
        Integer peerId = message.getPeerId();
        return userService.findByPeerId(peerId)
                .orElseGet(() -> UserModel.builder().peerId(peerId).build());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
