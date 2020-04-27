package by.mercury.vkontakte.job;

import by.mercury.core.command.CommandContext;
import by.mercury.core.command.CommandPreprocessor;
import by.mercury.core.job.Job;
import by.mercury.core.service.CommandContextService;
import by.mercury.core.service.CommandService;
import by.mercury.core.service.MessageReceiveService;
import com.vk.api.sdk.objects.messages.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Job} which receives new messages and find command for them
 *
 * @author Yegor Ikbaev
 */
@Slf4j
@Component
public class ReceiveMessagesJob implements Job {

    private MessageReceiveService<Message> messageReceiveService;

    private CommandContextService<Message> commandContextService;

    private List<CommandPreprocessor> preprocessors = Collections.emptyList();

    private CommandService commandService;

    @Scheduled(initialDelay = 1000, fixedDelay = 2000)
    @Override
    public void execute() {
        messageReceiveService.receiveAll().stream()
                .map(commandContextService::build)
                .map(this::preprocess)
                .forEachOrdered(this::executeCommand);
    }

    private CommandContext preprocess(CommandContext context) {
        try {
            preprocessors.forEach(preprocessor -> preprocessor.preprocess(context));
        } catch (IllegalStateException exception) {
            log.warn(exception.getMessage());
        }
        return context;
    }

    private void executeCommand(CommandContext context) {
        try {
            commandService.resolve(context).execute(context);
        } catch (IllegalStateException exception) {
            log.warn(exception.getMessage());
        }
    }

    @Autowired
    public void setMessageReceiveService(MessageReceiveService<Message> messageReceiveService) {
        this.messageReceiveService = messageReceiveService;
    }

    @Autowired
    public void setCommandContextService(CommandContextService<Message> commandContextService) {
        this.commandContextService = commandContextService;
    }

    @Autowired(required = false)
    public void setPreprocessors(List<CommandPreprocessor> preprocessors) {
        this.preprocessors = preprocessors.stream()
                .sorted(Comparator.comparingInt(CommandPreprocessor::getPriority))
                .collect(Collectors.toList());
    }

    @Autowired
    public void setCommandService(CommandService commandService) {
        this.commandService = commandService;
    }
}
