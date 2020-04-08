package by.mercury.core.converter;

import by.mercury.core.data.UserData;
import by.mercury.core.model.UserModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A converter converts a source object of type {@link UserData} to a target of type {@link UserModel}
 */
@Component
public class UserModelConverter implements Converter<UserModel, UserData> {

    /**
     * Convert the source object of type {@link UserModel} to target type {{@link UserData}
     *
     * @param source the source object to convert,
     *               which must be an instance of {@link UserModel} (never {@code null})
     * @return the converted object, which must be an instance of {{@link UserData}
     * @throws NullPointerException if the source or it's fields are null
     */
    @Override
    public UserData convert(UserModel source) {
        return UserData.builder()
                .id(source.getId())
                .peerId(source.getPeerId())
                .build();
    }
}
