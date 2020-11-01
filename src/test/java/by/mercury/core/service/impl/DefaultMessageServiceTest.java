package by.mercury.core.service.impl;

import by.mercury.core.data.MessageType;
import by.mercury.core.exception.SendMessageException;
import by.mercury.core.model.Channel;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.strategy.SendMessageStrategy;
import by.mercury.vkontakte.strategy.impl.VkTextSendMessageStrategy;
import by.mercury.vkontakte.strategy.impl.VkVoiceSendMessageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DefaultMessageServiceTest {

    private static final String EXCEPTION_MESSAGE = "message";
    private static final String MESSAGE_TEXT = "text";
    
    private DefaultMessageService testedInstance = new DefaultMessageService();

    private List<SendMessageStrategy> sendMessageStrategies;
    
    @Mock
    private VkTextSendMessageStrategy vkTextSendMessageStrategy;
    
    @Mock
    private VkVoiceSendMessageStrategy vkVoiceSendMessageStrategy; 
    
    @Mock
    private MessageModel message;

    @Mock
    private UserModel target;

    @BeforeEach
    public void setUp() {
        sendMessageStrategies = Arrays.asList(vkTextSendMessageStrategy, vkVoiceSendMessageStrategy);
        testedInstance.setSendMessageStrategies(sendMessageStrategies);
        when(message.getTarget()).thenReturn(target);
        when(message.getText()).thenReturn(MESSAGE_TEXT);
        when(vkTextSendMessageStrategy.support(anyCollection())).thenReturn(true);
        when(vkVoiceSendMessageStrategy.support(anyCollection())).thenReturn(true);
        when(vkTextSendMessageStrategy.support(MessageType.TEXT)).thenReturn(true);
        when(vkVoiceSendMessageStrategy.support(MessageType.VOICE)).thenReturn(true);
    }

    @Test
    public void shouldSendMessageIfPresent() {
        when(message.getTypes()).thenReturn(Arrays.asList(MessageType.TEXT, MessageType.VOICE));

        assertDoesNotThrow(() -> testedInstance.send(message));
        verify(vkTextSendMessageStrategy).send(eq(message));
        verify(vkVoiceSendMessageStrategy).send(eq(message));
    }

    @Test
    public void shouldThrowExceptionIfNotPresent() {
        when(message.getTypes()).thenReturn(Collections.singleton(MessageType.TEXT));
        doThrow(new SendMessageException(EXCEPTION_MESSAGE, new IllegalArgumentException()))
                .when(vkTextSendMessageStrategy).send(eq(message));
        
        SendMessageException actual = assertThrows(SendMessageException.class, () -> testedInstance.send(message));

        assertEquals(EXCEPTION_MESSAGE, actual.getMessage());
    }
}
