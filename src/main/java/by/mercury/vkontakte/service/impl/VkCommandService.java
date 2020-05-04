package by.mercury.vkontakte.service.impl;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import by.mercury.core.service.CommandService;
import by.mercury.vkontakte.command.VkUnknownCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Vkontakte test implementation of {@link CommandService}
 *
 * @author Yegor Ikbaev
 */
@Slf4j
@Service
public class VkCommandService implements CommandService {

    private VkUnknownCommand vkUnknownCommand;

    private List<Command> commands;

    @Override
    public Command resolve(CommandContext context) {
        log.info("Received message: {}", context.getMessage().getText());
        return commands.stream()
                .filter(command -> command.support(context))
                .findFirst()
                .orElse(vkUnknownCommand);
    }

    @Override
    public Command getCommandOnError() {
        return vkUnknownCommand;
    }

    @Autowired
    public void setVkUnknownCommand(VkUnknownCommand vkUnknownCommand) {
        this.vkUnknownCommand = vkUnknownCommand;
    }

    @Autowired
    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}
