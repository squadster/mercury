package by.mercury.vkontakte.preprocessor;

import by.mercury.core.command.CommandContext;
import by.mercury.core.command.CommandPreprocessor;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RegistrationPreprocessor implements CommandPreprocessor {

    public static final Integer PRIORITY = 10;

    public static final String IS_FIRST_MESSAGE = "isFirstMessage";

    private UserService userService;

    @Override
    public void preprocess(CommandContext context) {
        UserModel author = context.getMessage().getAuthor();
        if (isRegistered(author)) {
            context.getParameters().putIfAbsent(IS_FIRST_MESSAGE, Boolean.FALSE);
        } else {
            //TODO replace userId of id from API
            author.setUserId(author.getPeerId().longValue());
            context.getMessage().setAuthor(userService.save(author));
            context.getParameters().putIfAbsent(IS_FIRST_MESSAGE, Boolean.TRUE);
        }
    }

    private boolean isRegistered(UserModel user) {
        return !Objects.isNull(user.getId());
    }

    @Override
    public Integer getPriority() {
        return PRIORITY;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
