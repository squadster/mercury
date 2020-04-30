package by.mercury.vkontakte.service.impl;

import by.mercury.core.command.CommandContext;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.CommandContextService;
import by.mercury.core.service.UserService;
import by.mercury.vkontakte.context.VkCommandContext;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Vkontakte implementation of {@link CommandContextService}
 *
 * @author Yegor Ikbaev
 */
@Service
public class VkCommandContextService implements CommandContextService<Message> {
    
    public static final String VK_MESSAGE = "vkMessage";

    private UserService userService;

    @Override
    public CommandContext build(Message message) {
        Assert.notNull(message, "Message must not be null");

        return VkCommandContext.builder()
                .message(createMessageModel(message))
                .parameters(parameters(message))
                .build();
    }

    private MessageModel createMessageModel(Message message) {
        return MessageModel.builder()
                .text(message.getText())
                .author(gerOrCreateAuthor(message))
                .build();
    }

    private UserModel gerOrCreateAuthor(Message message) {
        var peerId = message.getPeerId();
        return userService.findByPeerId(peerId)
                .orElseGet(() -> UserModel.builder().peerId(peerId).uid(peerId.toString()).build());
    }
    
    private Map<String, Object> parameters(Message message) {
        var map = new HashMap<String, Object>();
        map.put(VK_MESSAGE, message);
        return map;
    } 

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
