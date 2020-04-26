package by.mercury.core.strategy.impl;

import by.mercury.core.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.VoiceId;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class AmazonPollySynthesizeSpeechStrategy extends AbstractSynthesizeSpeechStrategy {

    private PollyClient pollyClient;
    
    @Override
    public File synthesize(MessageModel message) {
        try {
            var file = createTempFile(message);
            var request = SynthesizeSpeechRequest.builder()
                    .outputFormat(OutputFormat.MP3)
                    .voiceId(VoiceId.MAXIM)
                    .text(message.getText())
                    .build();
            return save(file, pollyClient.synthesizeSpeech(request).readAllBytes());
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    @Autowired
    public void setPollyClient(PollyClient pollyClient) {
        this.pollyClient = pollyClient;
    }
}
