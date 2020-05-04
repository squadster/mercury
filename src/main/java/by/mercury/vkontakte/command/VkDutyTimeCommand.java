package by.mercury.vkontakte.command;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.MessageService;
import by.mercury.vkontakte.command.data.QueueNumberData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class VkDutyTimeCommand implements Command {

    private static final String CURRENT_WEEK_MESSAGE = "Вы дежурный на этой недели";
    private static final String MESSAGE_FORMAT = "Вы дежурный через %d недели";
    private final List<String> keyWords = Arrays.asList("duty", "vigil", "daily", "order", "dude");

    private MessageService messageService;

    private String apiUrl;
    
    private RestTemplate restTemplate;

    @Override
    public boolean support(CommandContext context) {
        return keyWords.stream()
                .anyMatch(context.getMessage().getText().toLowerCase()::contains);
    }

    @Override
    public void execute(CommandContext context) {
        var message = MessageModel.builder()
                .text(dutyTimeText(context.getMessage().getAuthor()))
                .target(context.getMessage().getAuthor())
                .types(Collections.singletonList(MessageType.VOICE))
                .build();
        messageService.send(message);
    }


    private String dutyTimeText(UserModel user) {
        var queue = restTemplate.getForObject(apiUrl + user.getPeerId(), QueueNumberData.class).getQueueNumber();
        var weeks = queue - 1;
        if  (weeks == 0) {
            return CURRENT_WEEK_MESSAGE;
        }
        return String.format(MESSAGE_FORMAT, weeks);
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${api.url}")
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
