package by.mercury.vkontakte.job;

import by.mercury.core.command.Command;
import by.mercury.core.command.CommandContext;
import by.mercury.core.command.CommandPreprocessor;
import by.mercury.core.service.CommandContextService;
import by.mercury.core.service.CommandService;
import by.mercury.core.service.MessageReceiveService;
import com.vk.api.sdk.objects.messages.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReceiveMessagesJobTest {

    @InjectMocks
    private ReceiveMessagesJob testedInstance;

    @Mock
    private MessageReceiveService<Message> messageReceiveService;
    @Mock
    private CommandContextService<Message> commandContextService;
    @Mock
    private CommandService commandService;
    @Mock
    private CommandPreprocessor preprocessor;

    @Mock
    private Message message;
    @Mock
    private CommandContext context;
    @Mock
    private Command command;

    @BeforeEach
    public void setUp() {
        when(messageReceiveService.receiveAll()).thenReturn(Collections.singletonList(message));
        when(commandContextService.build(eq(message))).thenReturn(context);
        when(commandService.resolve(eq(context))).thenReturn(command);
    }

    @Test
    public void shouldCallCommandIfPresent() {
        testedInstance.execute();

        verify(command).execute(eq(context));
    }
}
