package by.mercury.vkontakte.converter;

import by.mercury.core.model.UserModel;
import com.vk.api.sdk.objects.messages.Conversation;
import com.vk.api.sdk.objects.messages.ConversationPeer;
import com.vk.api.sdk.objects.messages.ConversationWithMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ConversationToUserModelConverterTest {

    private static final Integer PEER_ID = 0;

    private ConversationToUserModelConverter testedInstance;

    @Mock
    private ConversationWithMessage source;

    @Mock
    private Conversation conversation;

    @Mock
    private ConversationPeer conversationPeer;

    @BeforeEach
    public void setUp() {
        testedInstance = new ConversationToUserModelConverter();
        when(source.getConversation()).thenReturn(conversation);
        when(conversation.getPeer()).thenReturn(conversationPeer);
        when(conversationPeer.getId()).thenReturn(PEER_ID);
    }

    @Test
    public void shouldReturnUserModelIfPresent() {
        UserModel actual = testedInstance.convert(source);

        assertEquals(PEER_ID, actual.getPeerId());
    }
}
