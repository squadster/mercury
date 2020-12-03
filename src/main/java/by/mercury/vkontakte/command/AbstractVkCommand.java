package by.mercury.vkontakte.command;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import by.mercury.core.model.Channel;
import by.mercury.core.service.MessageService;
import by.mercury.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVkCommand implements Command {

    private MessageService messageService;
    
    private UserService userService;
    
    private List<String> keyWords;

    public AbstractVkCommand(List<String> keyWords) {
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

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }
}
