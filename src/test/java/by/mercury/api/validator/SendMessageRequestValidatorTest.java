package by.mercury.api.validator;

import by.mercury.api.request.SendMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class SendMessageRequestValidatorTest {

    private SendMessageRequestValidator testedInstance;

    @Mock
    private Errors errors;

    @Mock
    private SendMessageRequest message;

    @BeforeEach
    public void setUp() {
        testedInstance = new SendMessageRequestValidator();
    }

    @Test
    public void shouldReturnTrueIfClassSupports() {
        boolean actual = testedInstance.supports(SendMessageRequest.class);

        assertTrue(actual);
    }

    @Test
    public void shouldSetErrorsIfNotPresent() {
        testedInstance.validate(message, errors);

        verify(errors).rejectValue(anyString(), anyString(), isNull());
    }
}
