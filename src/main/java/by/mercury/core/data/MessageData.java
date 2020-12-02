package by.mercury.core.data;

import by.mercury.core.model.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * DTO class for {@link by.mercury.core.model.MessageModel}
 *
 * @author Yegor Ikbaev
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageData {

    private String text;

    private Collection<Channel> targetChannels;

    private UserData author;

    private UserData target;
    
    private Collection<MessageType> types;
}
