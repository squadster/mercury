package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import by.mercury.core.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

import static by.mercury.vkontakte.preprocessor.RegistrationPreprocessor.IS_FIRST_MESSAGE;

@Component
public class VkGenerateTokenCommand extends AbstractVkCommand {

    private UserService userService;
    
    public VkGenerateTokenCommand(MessageService messageService, RestTemplate restTemplate, UserService userService) {
        super(messageService, restTemplate, Arrays.asList("telegram", "token"));
        this.userService = userService;
    }

    @Override
    public void execute(CommandContext context) {
        var message = MessageModel.builder()
                .text(generateToken(context))
                .target(context.getMessage().getAuthor())
                .types(Collections.singletonList(MessageType.TEXT))
                .targetChannels(Collections.singleton(Channel.VK))
                .build();
        getMessageService().send(message);
    }
    
    private String generateToken(CommandContext context) {
        if (!Boolean.parseBoolean(context.getParameters().get(IS_FIRST_MESSAGE).toString())) {
            return "You are not registered";
        }
        var user = context.getMessage().getAuthor();
        var token = user.getPeerId().toString();
        user.setTelegramToken(token);
        userService.save(user);
        return token;
    }
}
