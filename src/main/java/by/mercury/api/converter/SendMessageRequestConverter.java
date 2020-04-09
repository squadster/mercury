package by.mercury.api.converter;

import by.mercury.api.request.SendMessageRequest;
import by.mercury.core.data.MessageData;
import by.mercury.core.data.MessageType;
import by.mercury.core.data.UserData;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A converter converts a source object of type {@link SendMessageRequest} to a target of type {@link MessageData}
 */
@Component
public class SendMessageRequestConverter implements Converter<SendMessageRequest, MessageData> {

    private UserService userService;

    private Converter<UserModel, UserData> converter;

    /**
     * Convert the source object of type {@link SendMessageRequest} to target type {{@link MessageData}
     *
     * @param source the source object to convert,
     *               which must be an instance of {@link SendMessageRequest} (never {@code null})
     * @return the converted object, which must be an instance of {{@link MessageData}
     * @throws NullPointerException     if the source or it's fields are null
     * @throws IllegalArgumentException if target is incorrect
     */
    @Override
    public MessageData convert(SendMessageRequest source) {
        UserData target = userService.findById(source.getTarget())
                .map(converter::convert)
                .orElseThrow(IllegalArgumentException::new);
        return MessageData.builder()
                .text(source.getText())
                .target(target)
                .types(convert(source.getTypes()))
                .build();
    }
    
    private Collection<MessageType> convert(Collection<String> types) {
        return Optional.ofNullable(types).stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(MessageType::toMessageType)
                .collect(Collectors.toSet());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setConverter(Converter<UserModel, UserData> converter) {
        this.converter = converter;
    }
}
