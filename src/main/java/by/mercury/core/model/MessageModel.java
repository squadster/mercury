package by.mercury.core.model;

import by.mercury.core.data.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Describes messages
 *
 * @author Yegor Ikbaev
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {

    private String text;

    private UserModel author;

    private UserModel target;
    
    private Collection<MessageType> types;
    
    private Channel sourceChannel;
    
    private Collection<Channel> targetChannels;
}
