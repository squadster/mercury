package by.mercury.vkontakte.command;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Vkontakte test implementation of {@link Command}
 *
 * @author Yegor Ikbaev
 */
@Component
public class VkUnknownCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkUnknownCommand.class.getName());

    @Override
    public void execute(CommandContext context) {
        LOGGER.info(context.getMessage().getText());
        LOGGER.info(context.getMessage().getAuthor().getPeerId().toString());
    }
}
