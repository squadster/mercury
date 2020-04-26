package by.mercury.core.service;

import by.mercury.core.model.MessageModel;

import java.io.File;

/**
 * Strategy for synthesize voice messages.
 *
 * @author Yegor Ikbaev
 */
public interface SynthesizeSpeechService {
    
    File synthesize(MessageModel message);
}
