package by.mercury.core.converter;

import by.mercury.core.data.MessageData;
import by.mercury.core.data.MessageType;
import by.mercury.core.data.UserData;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MessageDataConverterTest {

    private static final String TEXT = "text";

    @InjectMocks
    private MessageDataConverter testedInstance;

    @Mock
    private UserDataConverter userDataConverter;

    @Mock
    private MessageData source;

    @Mock
    private UserData author;

    @Mock
    private UserData target;

    @Mock
    private UserModel authorModel;

    @Mock
    private UserModel targetModel;
    
    private Collection<MessageType> types = Collections.singleton(MessageType.TEXT);

    @BeforeEach
    public void setUp() {
        when(source.getText()).thenReturn(TEXT);
        when(source.getAuthor()).thenReturn(author);
        when(source.getTarget()).thenReturn(target);
        when(source.getTypes()).thenReturn(types);
        when(userDataConverter.convert(eq(author))).thenReturn(authorModel);
        when(userDataConverter.convert(target)).thenReturn(targetModel);
    }

    @Test
    public void shouldReturnMessageModelIfPresent() {
        MessageModel actual = testedInstance.convert(source);

        assertEquals(TEXT, actual.getText());
        assertEquals(targetModel, actual.getTarget());
        assertEquals(types, actual.getTypes());
    }
}
