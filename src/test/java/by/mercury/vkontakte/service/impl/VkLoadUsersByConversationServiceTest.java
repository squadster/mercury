package by.mercury.vkontakte.service.impl;

import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import com.vk.api.sdk.actions.Messages;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.enums.MessagesFilter;
import com.vk.api.sdk.objects.messages.ConversationWithMessage;
import com.vk.api.sdk.objects.messages.responses.GetConversationsResponse;
import com.vk.api.sdk.queries.messages.MessagesGetConversationsQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VkLoadUsersByConversationServiceTest {

    private static final Integer EXISTED_ID = 1;
    private static final Integer NOT_EXISTED_ID = 0;
    private static final Integer INITIAL_OFFSET = 0;
    private static final Integer EMPTY_QUERY_OFFSET = 2;

    @InjectMocks
    private VkLoadUsersByConversationService testedInstance;

    @Mock
    private UserService userService;
    @Mock
    private VkApiClient vkApiClient;
    @Mock
    private GroupActor vkGroupActor;
    @Mock
    private Converter<ConversationWithMessage, UserModel> converter;
    @Mock
    private Messages messages;
    @Mock
    private MessagesGetConversationsQuery query;
    @Mock
    private MessagesGetConversationsQuery emptyQuery;
    @Mock
    private GetConversationsResponse response;
    @Mock
    private GetConversationsResponse emptyResponse;
    @Mock
    private ConversationWithMessage existedConversation;
    @Mock
    private ConversationWithMessage notExistedConversation;
    @Mock
    private UserModel existedUser;
    @Mock
    private UserModel notExistedUser;

    @BeforeEach
    public void setUp() throws ClientException, ApiException {
        when(vkApiClient.messages()).thenReturn(messages);
        when(messages.getConversations(eq(vkGroupActor))).thenReturn(query);
        when(query.filter(MessagesFilter.ALL)).thenReturn(query);
        when(query.count(anyInt())).thenReturn(query);
        when(query.offset(INITIAL_OFFSET)).thenReturn(query);
        when(query.offset(EMPTY_QUERY_OFFSET)).thenReturn(emptyQuery);
        when(query.execute()).thenReturn(response);
        when(emptyQuery.execute()).thenReturn(emptyResponse);
        when(response.getItems()).thenReturn(Arrays.asList(existedConversation, notExistedConversation));
        when(emptyResponse.getItems()).thenReturn(Collections.emptyList());
        when(converter.convert(eq(existedConversation))).thenReturn(existedUser);
        when(converter.convert(eq(notExistedConversation))).thenReturn(notExistedUser);
        when(userService.findByPeerId(EXISTED_ID)).thenReturn(Optional.of(existedUser));
        when(userService.findByPeerId(NOT_EXISTED_ID)).thenReturn(Optional.empty());
        when(existedUser.getPeerId()).thenReturn(EXISTED_ID);
        when(notExistedUser.getPeerId()).thenReturn(NOT_EXISTED_ID);
    }

    @Test
    public void shouldLoadNotExistedUsers() {
        testedInstance.load();

        verify(userService).saveAll(Collections.singletonList(notExistedUser));
    }
}
