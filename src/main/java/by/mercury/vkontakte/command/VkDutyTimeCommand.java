package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.MessageService;
import by.mercury.vkontakte.command.data.QueueNumberData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Component
public class VkDutyTimeCommand extends AbstractVkCommand {

    private static final String CURRENT_WEEK_MESSAGE = "Вы дежурный на этой недели";
    private static final String MESSAGE_FORMAT = "Вы дежурный через %d недели";
    
    private String apiUrl;
    
    public VkDutyTimeCommand(MessageService messageService, RestTemplate restTemplate) {
        super(messageService, restTemplate, Arrays.asList("duty", "vigil", "daily", "order", "dude"));
    }

    @Override
    public void execute(CommandContext context) {
        var message = MessageModel.builder()
                .text(dutyTimeText(context.getMessage().getAuthor()))
                .target(context.getMessage().getAuthor())
                .types(Collections.singletonList(MessageType.VOICE))
                .targetChannels(Collections.singleton(Channel.VK))
                .build();
        getMessageService().send(message);
    }


    private String dutyTimeText(UserModel user) {
        var queue = getRestTemplate().getForObject(apiUrl + user.getPeerId(), QueueNumberData.class).getQueueNumber();
        var weeks = queue - 1;
        if  (weeks == 0) {
            return CURRENT_WEEK_MESSAGE;
        }
        return String.format(MESSAGE_FORMAT, weeks);
    }

    @Value("${api.url}")
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
