package by.mercury.vkontakte.service.impl;

import com.vk.api.sdk.actions.Messages;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.LongpollMessages;
import com.vk.api.sdk.objects.messages.LongpollParams;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.responses.GetLongPollHistoryResponse;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollServerQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VkReceiveMessageServiceTest {

    private static final Integer GROUP_ID = -1;
    private static final Integer USER_ID = 0;
    private static final Integer TS = -1;

    @InjectMocks
    private VkReceiveMessageService testedInstance;

    @Mock
    private VkApiClient vkApiClient;

    @Mock
    private GroupActor vkGroupActor;

    @Mock
    private MessagesGetLongPollHistoryQuery query;
    @Mock
    private Messages messages;
    @Mock
    private GetLongPollHistoryResponse response;
    @Mock
    private LongpollMessages longpollMessages;
    @Mock
    private Message message;
    @Mock
    private MessagesGetLongPollServerQuery serverQuery;
    @Mock
    private LongpollParams params;

    @BeforeEach
    public void setUp() throws ClientException, ApiException {
        when(vkApiClient.messages()).thenReturn(messages);
        when(messages.getLongPollHistory(eq(vkGroupActor))).thenReturn(query);
        when(query.ts(TS)).thenReturn(query);
        when(query.execute()).thenReturn(response);
        when(response.getMessages()).thenReturn(longpollMessages);
        when(longpollMessages.getItems()).thenReturn(Collections.singletonList(message));
        when(message.getFromId()).thenReturn(USER_ID);
        when(vkGroupActor.getGroupId()).thenReturn(GROUP_ID);
        when(messages.getLongPollServer(eq(vkGroupActor))).thenReturn(serverQuery);
        when(serverQuery.execute()).thenReturn(params);
        when(params.getTs()).thenReturn(TS);
    }

    @Test
    public void shouldReturnMessages() {
        List<Message> actual = testedInstance.receiveAll();

        List<Message> expected = Collections.singletonList(message);
        assertEquals(expected, actual);
    }
}
