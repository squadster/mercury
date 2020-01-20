package by.mercury.vkontakte.runner;

import by.mercury.core.service.LoadUsersByConversationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;

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
}
