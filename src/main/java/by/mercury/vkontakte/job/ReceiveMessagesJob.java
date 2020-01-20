package by.mercury.vkontakte.job;

import by.mercury.core.command.CommandContext;
import by.mercury.core.job.Job;
import by.mercury.core.service.CommandContextService;
import by.mercury.core.service.CommandService;
import by.mercury.core.service.MessageReceiveService;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link Job} which receives new messages and find command for them
 *
 * @author Yegor Ikbaev
 */
@Component
public class ReceiveMessagesJob implements Job {

    private MessageReceiveService<Message> messageReceiveService;

    private CommandContextService<Message> commandContextService;

    private CommandService commandService;

    @Scheduled(initialDelay = 1000, fixedDelay = 10000)
    @Override
    public void execute() {
        messageReceiveService.receiveAll().stream()
                .map(commandContextService::build)
                .forEachOrdered(this::executeCommand);
    }

    private void executeCommand(CommandContext context) {
        commandService.resolve(context).execute(context);
    }

    @Autowired
    public void setMessageReceiveService(MessageReceiveService<Message> messageReceiveService) {
        this.messageReceiveService = messageReceiveService;
    }

    @Autowired
    public void setCommandContextService(CommandContextService<Message> commandContextService) {
        this.commandContextService = commandContextService;
    }

    @Autowired
    public void setCommandService(CommandService commandService) {
        this.commandService = commandService;
    }
}
