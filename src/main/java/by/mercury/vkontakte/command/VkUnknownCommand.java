package by.mercury.vkontakte.command;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Vk test implementation of {@link Command}
 *
 * @author Yegor Ikbaev
 */
@Component
public class VkUnknownCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkUnknownCommand.class.getName());
    private static final String COMMAND_PATTERN = "";

    @Override
    public void execute(CommandContext context) {
        LOGGER.info(context.getMessage().getText());
        LOGGER.info(context.getMessage().getAuthor().getPeerId().toString());
    }

    @Override
    public String getPattern() {
        return COMMAND_PATTERN;
    }
}
