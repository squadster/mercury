package by.mercury.vkontakte.service.impl;

import by.mercury.core.data.MessageType;
import by.mercury.core.exception.SendMessageException;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.strategy.SendMessageStrategy;
import by.mercury.vkontakte.strategy.impl.TextSendMessageStrategy;
import by.mercury.vkontakte.strategy.impl.VoiceSendMessageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VkMessageServiceTest {

    private static final String EXCEPTION_MESSAGE = "message";
    private static final String MESSAGE_TEXT = "text";
    
    private VkMessageService testedInstance = new VkMessageService();

    private Map<MessageType, SendMessageStrategy> sendMessageStrategies;
    
    @Mock
    private TextSendMessageStrategy textSendMessageStrategy;
    
    @Mock
    private VoiceSendMessageStrategy voiceSendMessageStrategy; 
    
    @Mock
    private MessageModel message;

    @Mock
    private UserModel target;

    @BeforeEach
    public void setUp() {
        sendMessageStrategies = Map.of(MessageType.TEXT, textSendMessageStrategy, 
                MessageType.VOICE, voiceSendMessageStrategy);
        testedInstance.setSendMessageStrategies(sendMessageStrategies);
        when(message.getTarget()).thenReturn(target);
        when(message.getText()).thenReturn(MESSAGE_TEXT);
    }

    @Test
    public void shouldSendMessageIfPresent() {
        when(message.getTypes()).thenReturn(Arrays.asList(MessageType.TEXT, MessageType.VOICE));

        assertDoesNotThrow(() -> testedInstance.send(message));
        verify(textSendMessageStrategy).send(eq(message));
        verify(voiceSendMessageStrategy).send(eq(message));
    }

    @Test
    public void shouldThrowExceptionIfNotPresent() {
        when(message.getTypes()).thenReturn(Collections.singleton(MessageType.TEXT));
        doThrow(new SendMessageException(EXCEPTION_MESSAGE, new IllegalArgumentException()))
                .when(textSendMessageStrategy).send(eq(message));
        
        SendMessageException actual = assertThrows(SendMessageException.class, () -> testedInstance.send(message));

        assertEquals(EXCEPTION_MESSAGE, actual.getMessage());
    }
}
