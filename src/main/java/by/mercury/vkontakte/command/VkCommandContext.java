package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.model.MessageModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VkCommandContext implements CommandContext {

    private MessageModel message;
}
