package by.mercury.core.strategy.impl;

import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest
public class LocalMarySynthesizeSpeechStrategyTest {

    private static final String AUDIO_MESSAGE_FILE_EXTENSION = ".wav";
    private static final String MESSAGE_TEXT = "text";
    private static final Integer PEER_ID = 12345678;

    private LocalMarySynthesizeSpeechStrategy testedInstance;

    @Mock
    private MessageModel message;

    @Mock
    private UserModel target;

    @BeforeEach
    public void setUp() throws MaryConfigurationException {
        testedInstance = new LocalMarySynthesizeSpeechStrategy();
        testedInstance.setLocalMaryInterface(new LocalMaryInterface());

        when(message.getText()).thenReturn(MESSAGE_TEXT);
        when(message.getTarget()).thenReturn(target);
        when(target.getPeerId()).thenReturn(PEER_ID);
    }

    @Test
    public void shouldReturnNonNullFile() {
        var actual = testedInstance.synthesize(message);

        assertNotNull(actual);
    }

    @Test
    public void shouldReturnWavExtension() {
        var actual = testedInstance.getFileExtension();

        assertEquals(AUDIO_MESSAGE_FILE_EXTENSION, actual);
    }
}
