package by.mercury.vkontakte.preprocessor;

import by.mercury.core.command.CommandContext;
import by.mercury.core.model.UserModel;

import java.util.Objects;

public class RegistrationPreprocessor extends AbstractVkPreprocessor {

    public static final Integer PRIORITY = 10;

    public static final String IS_FIRST_MESSAGE = "isFirstMessage";
    
    @Override
    public void preprocess(CommandContext context) {
        var author = context.getMessage().getAuthor();
        context.getParameters().putIfAbsent(IS_FIRST_MESSAGE, isRegistered(author));
    }

    private boolean isRegistered(UserModel user) {
        return Objects.nonNull(user.getId());
    }

    @Override
    public Integer getPriority() {
        return PRIORITY;
    }
}
