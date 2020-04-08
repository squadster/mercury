package by.mercury.api.validator;

import by.mercury.api.request.SendMessageRequest;
import by.mercury.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

/**
 * Validates {@link SendMessageRequest} objects
 *
 * @author Yegor Ikbaev
 */
@Component
@PropertySource("classpath:message.properties")
public class SendMessageRequestValidator implements Validator {

    private static final String TEXT_FIELD = "text";
    private static final String TARGET_FIELD = "target";

    private static final String EMPTY_TEXT_ERROR = "sendMessageData.text.empty";
    private static final String EMPTY_TARGET_ERROR = "sendMessageData.target.empty";
    private static final String INVALID_TARGET_ERROR = "sendMessageData.target.invalid";

    @Value("${sendMessageData.text.empty}")
    private String defaultEmptyTextMessage;
    @Value("${sendMessageData.target.empty}")
    private String defaultEmptyTargetMessage;
    @Value("${sendMessageData.target.invalid}")
    private String defaultInvalidTargetMessage;

    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SendMessageRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SendMessageRequest message = (SendMessageRequest) target;
        validateText(message, errors);
        validateTarget(message, errors);
    }

    private void validateText(SendMessageRequest message, Errors errors) {
        if (!StringUtils.hasText(message.getText())) {
            errors.rejectValue(TEXT_FIELD, EMPTY_TEXT_ERROR, defaultEmptyTextMessage);
        }
    }

    private void validateTarget(SendMessageRequest message, Errors errors) {
        if (Objects.isNull(message.getTarget())) {
            errors.rejectValue(TARGET_FIELD, EMPTY_TARGET_ERROR, defaultEmptyTargetMessage);
        } else {
            userService.findById(message.getTarget())
                    .ifPresentOrElse(user -> {
                            },
                            () -> errors.rejectValue(TARGET_FIELD, INVALID_TARGET_ERROR, defaultInvalidTargetMessage));
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
