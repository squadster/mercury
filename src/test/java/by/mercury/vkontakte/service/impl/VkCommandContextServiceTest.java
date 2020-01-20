package by.mercury.vkontakte.service.impl;

import by.mercury.core.command.CommandContext;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import com.vk.api.sdk.objects.messages.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VkCommandContextServiceTest {

    private static final String MESSAGE_TEXT = "text";
    private static final Integer PEER_ID = 1;
    private static final Long USER_ID = 1L;

    @InjectMocks
    private VkCommandContextService testedInstance;

    @Mock
    private UserService userService;

    @Mock
    private Message message;

    @Mock
    private UserModel user;

    @BeforeEach
    public void setUp() {
        when(message.getText()).thenReturn(MESSAGE_TEXT);
        when(message.getPeerId()).thenReturn(PEER_ID);
        when(userService.findByPeerId(PEER_ID)).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(USER_ID);
    }

    @Test
    public void shouldReturnCommandContextIfPresent() {
        CommandContext actual = testedInstance.build(message);

        assertEquals(MESSAGE_TEXT, actual.getMessage().getText());
        assertEquals(user, actual.getMessage().getAuthor());
    }
}
