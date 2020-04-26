package by.mercury.core.converter;

import by.mercury.core.data.UserData;
import by.mercury.core.model.UserModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A converter converts a source object of type {@link UserData} to a target of type {@link UserModel}
 */
@Component
public class UserDataConverter implements Converter<UserData, UserModel> {

    /**
     * Convert the source object of type {@link UserData} to target type {{@link UserModel}
     *
     * @param source the source object to convert,
     *               which must be an instance of {@link UserData} (never {@code null})
     * @return the converted object, which must be an instance of {{@link UserModel}
     * @throws NullPointerException if the source or it's fields are null
     */
    @Override
    public UserModel convert(UserData source) {
        return UserModel.builder()
                .id(source.getId())
                .peerId(source.getPeerId())
                .uid(source.getPeerId().toString())
                .build();
    }
}
