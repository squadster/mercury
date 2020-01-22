package by.mercury.api.controller;

import by.mercury.api.converter.SendMessageRequestConverter;
import by.mercury.api.request.SendMessageRequest;
import by.mercury.api.response.SendMessageResponse;
import by.mercury.core.exception.SendMessageException;
import by.mercury.core.facade.MessageFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class SendMessageRestController {

    private static final String ERROR_DELIMITER = ", ";

    private MessageFacade messageFacade;

    private Validator validator;

    private SendMessageRequestConverter converter;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping
    public ResponseEntity<SendMessageResponse> send(@RequestBody @Validated SendMessageRequest request, Errors result) {
        try {
            if (!result.hasErrors()) {
                messageFacade.send(converter.convert(request));
                return handleSuccessfulCase();
            } else {
                return handleErrorsCase(result);
            }
        } catch (SendMessageException exception) {
            return handleException(exception);
        }
    }

    private ResponseEntity<SendMessageResponse> handleSuccessfulCase() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(SendMessageResponse.builder().successful(Boolean.TRUE).build());
    }

    private ResponseEntity<SendMessageResponse> handleErrorsCase(Errors result) {
        String errors = result.getFieldErrors().stream()
                .map(FieldError::getRejectedValue)
                .map(ObjectUtils::nullSafeToString)
                .collect(Collectors.joining(ERROR_DELIMITER));
        SendMessageResponse response = SendMessageResponse.builder()
                .successful(Boolean.FALSE)
                .response(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ResponseEntity<SendMessageResponse> handleException(SendMessageException exception) {
        SendMessageResponse response = SendMessageResponse.builder()
                .successful(Boolean.FALSE)
                .response(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Autowired
    public void setMessageFacade(MessageFacade messageFacade) {
        this.messageFacade = messageFacade;
    }

    @Autowired
    @Qualifier("sendMessageRequestValidator")
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Autowired
    public void setConverter(SendMessageRequestConverter converter) {
        this.converter = converter;
    }
}
