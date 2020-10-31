package by.mercury.vkontakte.command;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import by.mercury.core.model.Channel;
import by.mercury.core.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVkCommand implements Command {

    private MessageService messageService;
    
    private RestTemplate restTemplate;

    private List<String> keyWords;

    public AbstractVkCommand(MessageService messageService, RestTemplate restTemplate, List<String> keyWords) {
        this.messageService = messageService;
        this.restTemplate = restTemplate;
        this.keyWords = new ArrayList<>(keyWords);
    }

    @Override
    public boolean support(Channel channel) {
        return channel == Channel.VK;
    }

    @Override
    public boolean support(CommandContext context) {
        return getKeyWords().stream()
                .anyMatch(context.getMessage().getText().toLowerCase()::contains);
    } 

    public MessageService getMessageService() {
        return messageService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }
}
