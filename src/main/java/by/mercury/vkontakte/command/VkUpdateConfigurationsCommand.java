package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class VkUpdateConfigurationsCommand extends AbstractVkCommand {

    private final UserService userService;
    
    public VkUpdateConfigurationsCommand(UserService userService) {
        super(Arrays.asList("настройки"));
        this.userService = userService;
    }

    @Override
    public void execute(CommandContext context) {
        var author = context.getMessage().getAuthor();
        update(context.getMessage().getText(), author);
        
        var message = MessageModel.builder()
                .text("Настройки обновлены")
                .target(author)
                .types(Collections.singletonList(MessageType.TEXT))
                .targetChannels(Collections.singleton(Channel.VK))
                .build();
        getMessageService().send(message);
    }
    
    private void update(String text, UserModel user) {
        var configurations = userService.getUserConfigurationForUser(user);
        Arrays.asList(text.split(",")).stream()
                .map(str -> str.replaceAll("[^\\s]*\\s+", ""))
                .map(str -> str.split("="))
                .filter(array -> array.length == 2)
                .forEach(array -> {
                    if (array[0].equals("speaker")) {
                        configurations.getSource().setSpeaker(array[1]);
                    }
                });
        userService.update(configurations.getSource());
    }

}
