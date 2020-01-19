package by.mercury.core.converter;

import by.mercury.core.data.MessageData;
import by.mercury.core.data.UserData;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A converter converts a source object of type {@link MessageData} to a target of type {@link MessageModel}
 */
@Component
public class MessageDataConverter implements Converter<MessageData, MessageModel> {

    private Converter<UserData, UserModel> userDataConverter;

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
                .target(userDataConverter.convert(source.getTarget()))
                .build();
    }

    @Autowired
    public void setUserDataConverter(Converter<UserData, UserModel> userDataConverter) {
        this.userDataConverter = userDataConverter;
    }
}
