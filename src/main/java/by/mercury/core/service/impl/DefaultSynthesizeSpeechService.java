package by.mercury.core.service.impl;

import by.mercury.core.model.MessageModel;
import by.mercury.core.service.SynthesizeSpeechService;
import by.mercury.core.strategy.SynthesizeSpeechStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Default implementation of {@link SynthesizeSpeechService}.
 *
 * @author Yegor Ikbaev
 */
@Service
public class DefaultSynthesizeSpeechService implements SynthesizeSpeechService {
    
    private SynthesizeSpeechStrategy speechStrategy;
    
    @Override
    public File synthesize(MessageModel message) {
        return speechStrategy.synthesize(message);
    }

    @Autowired
    public void setSpeechStrategy(SynthesizeSpeechStrategy speechStrategy) {
        this.speechStrategy = speechStrategy;
    }
}
