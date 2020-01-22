package by.mercury.api.validator;

import by.mercury.api.request.SendMessageRequest;
import by.mercury.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SendMessageRequestValidatorTest {

    @InjectMocks
    private SendMessageRequestValidator testedInstance;

    @Mock
    private Errors errors;

    @Mock
    private UserService userService;

    @Mock
    private SendMessageRequest message;

    @Test
    public void shouldReturnTrueIfClassSupports() {
        boolean actual = testedInstance.supports(SendMessageRequest.class);

        assertTrue(actual);
    }

    @Test
    public void shouldSetErrorsIfTargetAndTextNull() {
        when(message.getTarget()).thenReturn(null);

        testedInstance.validate(message, errors);

        verify(errors, atLeastOnce()).rejectValue(anyString(), anyString(), isNull());
    }

    @Test
    public void shouldSetErrorsIfTargetInvalid() {
        testedInstance.validate(message, errors);

        verify(errors, atLeastOnce()).rejectValue(anyString(), anyString(), isNull());
    }
}
