package by.mercury.vkontakte.service.impl;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import by.mercury.core.service.CommandService;
import by.mercury.vkontakte.command.VkUnknownCommand;
import org.springframework.stereotype.Service;

/**
 * Vkontakte test implementation of {@link CommandService}
 *
 * @author Yegor Ikbaev
 */
@Service
public class VkCommandService implements CommandService {
    @Override
    public Command resolve(CommandContext context) {
        return new VkUnknownCommand();
    }
}
