package by.mercury.vkontakte.context;

import by.mercury.core.command.CommandContext;
import by.mercury.core.model.MessageModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Vk implementation of {@link CommandContext}
 *
 * @author Yegor Ikbaev
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VkCommandContext implements CommandContext {

    private MessageModel message;

    private Map<String, Object> parameters;

    @Override
    public MessageModel getMessage() {
        return message;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }
}
