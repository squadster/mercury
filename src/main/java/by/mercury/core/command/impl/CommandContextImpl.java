package by.mercury.core.command.impl;

import by.mercury.core.command.CommandContext;
import by.mercury.core.model.MessageModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Implementation of {@link CommandContext}
 *
 * @author Yegor Ikbaev
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandContextImpl implements CommandContext {

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
