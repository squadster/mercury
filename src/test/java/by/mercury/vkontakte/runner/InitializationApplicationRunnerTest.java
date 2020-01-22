package by.mercury.vkontakte.runner;

import by.mercury.core.service.LoadUsersByConversationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class InitializationApplicationRunnerTest {

    @InjectMocks
    private InitializationApplicationRunner testedInstance;

    @Mock
    private LoadUsersByConversationService service;

    @Mock
    private ApplicationArguments args;

    @Test
    public void shouldCallServiceMethod() {
        testedInstance.run(args);

        verify(service).load();
    }

    @Test
    public void shouldReturnMaxPriority() {
        int actual = testedInstance.getOrder();

        assertEquals(Ordered.HIGHEST_PRECEDENCE, actual);
    }
}
