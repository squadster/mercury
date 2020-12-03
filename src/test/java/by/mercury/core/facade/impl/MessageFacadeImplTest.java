package by.mercury.core.facade.impl;

import by.mercury.core.data.MessageData;
import by.mercury.core.data.UserData;
import by.mercury.core.facade.impl.MessageFacadeImpl;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MessageFacadeImplTest {

    private static final String TEXT = "text";
    private static final Integer PEER_ID = 1;

    @InjectMocks
    private MessageFacadeImpl testedInstance;

    @Mock
    private MessageService messageService;

    @Mock
    private Converter<MessageData, MessageModel> converter;

    @Mock
    private MessageData message;

    @Mock
    private MessageModel messageModel;

    @Mock
    private UserData target;

    @BeforeEach
    public void setUp() {
        when(converter.convert(eq(message))).thenReturn(messageModel);
        when(message.getText()).thenReturn(TEXT);
        when(message.getTarget()).thenReturn(target);
        when(target.getPeerId()).thenReturn(PEER_ID);
    }

    @Test
    public void shouldCallServiceMethodIfPresent() {
        testedInstance.send(message);

        verify(messageService).send(eq(messageModel));
    }
}
