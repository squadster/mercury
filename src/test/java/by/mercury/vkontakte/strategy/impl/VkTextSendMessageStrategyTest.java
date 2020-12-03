package by.mercury.vkontakte.strategy.impl;

import by.mercury.core.exception.SendMessageException;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import com.vk.api.sdk.actions.Messages;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VkTextSendMessageStrategyTest {

    private static final String EXCEPTION_MESSAGE = "message";
    private static final String MESSAGE_TEXT = "text";

    @InjectMocks
    private VkTextSendMessageStrategy testedInstance;

    @Mock
    private VkApiClient vkApiClient;
    @Mock
    private GroupActor vkGroupActor;
    @Mock
    private Messages messages;
    @Mock
    private MessagesSendQuery query;

    @Mock
    private MessageModel message;

    @Mock
    private UserModel target;

    @BeforeEach
    public void setUp() {
        when(vkApiClient.messages()).thenReturn(messages);
        when(messages.send(eq(vkGroupActor))).thenReturn(query);
        when(query.peerId(anyInt())).thenReturn(query);
        when(query.message(MESSAGE_TEXT)).thenReturn(query);
        when(query.randomId(anyInt())).thenReturn(query);
        when(message.getTarget()).thenReturn(target);
        when(message.getText()).thenReturn(MESSAGE_TEXT);
    }

    @Test
    public void shouldSendMessageIfPresent() throws ClientException, ApiException {
        when(query.execute()).thenReturn(null);

        assertDoesNotThrow(() -> testedInstance.send(message));
    }

    @Test
    public void shouldThrowExceptionIfNotPresent() throws ClientException, ApiException {
        when(query.execute()).thenThrow(new ClientException(EXCEPTION_MESSAGE));

        SendMessageException actual = assertThrows(SendMessageException.class, () -> testedInstance.send(message));

        assertEquals(EXCEPTION_MESSAGE, actual.getMessage());
    }
}
