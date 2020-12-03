package by.mercury.core.converter;

import by.mercury.core.data.MessageData;
import by.mercury.core.data.UserData;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A converter converts a source object of type {@link MessageData} to a target of type {@link MessageModel}
 */
@Component
public class MessageDataConverter implements Converter<MessageData, MessageModel> {
    
    private final UserService userService;

    public MessageDataConverter(UserService userService) {
        this.userService = userService;
    }

    /**
     * Convert the source object of type {@link MessageData} to target type {{@link MessageModel}
     *
     * @param source the source object to convert,
     *               which must be an instance of {@link MessageData} (never {@code null})
     * @return the converted object, which must be an instance of {{@link MessageModel}
     * @throws NullPointerException if the source or it's fields are null
     */
    @Override
    public MessageModel convert(MessageData source) {
        return MessageModel.builder()
                .text(source.getText())
                .target(findUser(source.getTarget()))
                .types(source.getTypes())
                .targetChannels(source.getTargetChannels())
                .build();
    }

    private UserModel findUser(UserData user) {
        return userService.findById(user.getId())
                .or(() -> userService.findByPeerId(user.getPeerId()))
                .orElseThrow(() -> new IllegalArgumentException("There is no user for" + user));
    }
}
