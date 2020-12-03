package by.mercury.core.strategy.impl;

import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest
public class AmazonPollySynthesizeSpeechStrategyTest {

    private static final String MESSAGE_TEXT = "text";
    private static final Integer PEER_ID = 12345678;

    @InjectMocks
    private AmazonPollySynthesizeSpeechStrategy testedInstance;

    @Mock
    private PollyClient pollyClient;

    private ResponseInputStream<SynthesizeSpeechResponse> response;

    @Mock
    private MessageModel message;
    @Mock
    private UserModel target;

    @BeforeEach
    public void setUp() throws IOException {
        when(message.getText()).thenReturn(MESSAGE_TEXT);
        when(message.getTarget()).thenReturn(target);
        when(target.getPeerId()).thenReturn(PEER_ID);

        response = mock(ResponseInputStream.class);
        
        when(pollyClient.synthesizeSpeech(any(SynthesizeSpeechRequest.class))).thenReturn(response);
        when(response.readAllBytes()).thenReturn(new byte[0]);
    }

    @Test
    @Disabled
    public void shouldCallPollyClient() {
        testedInstance.synthesize(message);

        verify(pollyClient).synthesizeSpeech(any(SynthesizeSpeechRequest.class));
    }
}
