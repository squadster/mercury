package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

import static by.mercury.vkontakte.preprocessor.RegistrationPreprocessor.IS_FIRST_MESSAGE;

@Component
@Slf4j
public class VkGenerateTokenCommand extends AbstractVkCommand {

    private UserService userService;
    
    public VkGenerateTokenCommand(UserService userService) {
        super(Collections.singletonList("telegram"));
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
        log.info("Token generation command for VK was successfully completed");
    }
    
    private String generateToken(CommandContext context) {
        if (!Boolean.parseBoolean(context.getParameters().get(IS_FIRST_MESSAGE).toString())) {
            return "Вы не зарегистрированы в системе";
        }
        var user = context.getMessage().getAuthor();
        var token = UUID.randomUUID().toString();
        user.setTelegramToken(token);
        userService.save(user);
        return token;
    }
}
