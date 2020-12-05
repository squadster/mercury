package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class VkHelpCommand extends AbstractVkCommand {

    public VkHelpCommand() {
        super(Arrays.asList("help"));
    }

    @Override
    public void execute(CommandContext context) {
        var message = MessageModel.builder()
                .text("Команды бота:\nвключить - включает уведомления\nотключить - отключает уведомления" +
                        "\ntelegram - создать токен для регистрации в Telegram\nрасписание - узнать текущее расписание")
                .target(context.getMessage().getAuthor())
                .types(Collections.singletonList(MessageType.TEXT))
                .targetChannels(Collections.singleton(Channel.VK))
                .build();
        getMessageService().send(message);
    }
}
