package by.mercury.api.converter;

import by.mercury.api.request.SendMessageRequest;
import by.mercury.core.data.MessageData;
import by.mercury.core.data.UserData;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SendMessageRequestConverterTest {

    private static final String TEXT = "text";
    private static final Long VALID_TARGET = 1L;
    private static final Long INVALID_TARGET = 0L;

    @InjectMocks
    private SendMessageRequestConverter testedInstance;

    @Mock
    private UserService userService;

    @Mock
    private Converter<UserModel, UserData> converter;

    @Mock
    private SendMessageRequest source;

    @Mock
    private UserModel targetModel;

    @Mock
    private UserData target;

    @BeforeEach
    public void setUp() {
        when(userService.findById(VALID_TARGET)).thenReturn(Optional.of(targetModel));
        when(userService.findById(INVALID_TARGET)).thenReturn(Optional.empty());
        when(converter.convert(targetModel)).thenReturn(target);
        when(source.getText()).thenReturn(TEXT);
    }

    @Test
    public void shouldReturnMessageModelIfPresent() {
        when(source.getTarget()).thenReturn(VALID_TARGET);

        MessageData actual = testedInstance.convert(source);

        assertEquals(TEXT, actual.getText());
        assertEquals(target, actual.getTarget());
    }

    @Test
    public void shouldReturnExceptionIfNotPresent() {
        when(source.getTarget()).thenReturn(INVALID_TARGET);

        assertThrows(IllegalArgumentException.class, () -> testedInstance.convert(source));
    }
}
