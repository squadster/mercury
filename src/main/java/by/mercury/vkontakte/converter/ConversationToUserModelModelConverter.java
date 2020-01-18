package by.mercury.vkontakte.converter;

import by.mercury.core.model.UserModel;
import com.vk.api.sdk.objects.messages.ConversationWithMessage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A converter converts a source object of type {@link ConversationWithMessage} to a target of type {@link UserModel}
 */
@Component
public class ConversationToUserModelModelConverter implements Converter<ConversationWithMessage, UserModel> {

    /**
     * Convert the source object of type {@link ConversationWithMessage} to target type {{@link UserModel}
     *
     * @param source the source object to convert,
     *               which must be an instance of {@link ConversationWithMessage} (never {@code null})
     * @return the converted object, which must be an instance of {{@link UserModel}
     * @throws NullPointerException if the source or it's fields are null
     */
    @Override
    public UserModel convert(ConversationWithMessage source) {
        return UserModel.builder()
                .peerId(source.getConversation().getPeer().getId())
                .build();
    }
}
