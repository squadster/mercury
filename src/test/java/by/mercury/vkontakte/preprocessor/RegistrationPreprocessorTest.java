package by.mercury.vkontakte.preprocessor;

import by.mercury.core.command.CommandContext;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static by.mercury.vkontakte.preprocessor.RegistrationPreprocessor.IS_FIRST_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RegistrationPreprocessorTest {
    
    private static final Long ID = 1L;

    @InjectMocks
    private RegistrationPreprocessor testedInstance;
    
    @Mock
    private CommandContext context;
    @Mock
    private MessageModel message;
    @Mock
    private UserModel author;
    @Mock
    private Map<String, Object> parameters;

    @BeforeEach
    public void setUp() {
        when(context.getMessage()).thenReturn(message);
        when(context.getParameters()).thenReturn(parameters);
        when(message.getAuthor()).thenReturn(author);
    }
    
    @Test
    public void shouldPutTrueIfRegistered() {
        when(author.getId()).thenReturn(ID);

        testedInstance.preprocess(context);

        verify(parameters).putIfAbsent(eq(IS_FIRST_MESSAGE), eq(Boolean.TRUE));
    }

    @Test
    public void shouldReturnPriority() {
        var actual = testedInstance.getPriority();

        assertEquals(RegistrationPreprocessor.PRIORITY, actual);
    }
}
