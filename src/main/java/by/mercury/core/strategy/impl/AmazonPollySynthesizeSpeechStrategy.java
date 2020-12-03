package by.mercury.core.strategy.impl;

import by.mercury.core.model.MessageModel;
import by.mercury.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class AmazonPollySynthesizeSpeechStrategy extends AbstractSynthesizeSpeechStrategy {

    private PollyClient pollyClient;
    
    private UserService userService;

    @Override
    public File synthesize(MessageModel message) {
        try {
            var file = createTempFile(message);
            var configurations = userService.getUserConfigurationForUser(message.getTarget());
            var request = SynthesizeSpeechRequest.builder()
                    .outputFormat(OutputFormat.MP3)
                    .voiceId(configurations.getSpeaker())
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

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
