package by.mercury.core.service.impl;

import by.mercury.core.model.MessageModel;
import by.mercury.core.strategy.SynthesizeSpeechStrategy;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class DefaultSynthesizeSpeechServiceTest {
    
    @InjectMocks
    private DefaultSynthesizeSpeechService testedInstance;

    @Mock
    private SynthesizeSpeechStrategy synthesizeSpeechStrategy;
    
    @Mock
    private MessageModel message;
    
    @Test
    public void shouldDelegateToStrategy() {
        testedInstance.synthesize(message);
        
        verify(synthesizeSpeechStrategy).synthesize(eq(message));
    }
}
