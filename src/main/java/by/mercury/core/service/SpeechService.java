package by.mercury.core.service;

import by.mercury.core.model.MessageModel;

import java.io.File;

/**
 * Service for working with voice messages.
 *
 * @author Yegor Ikbaev
 */
public interface SpeechService {
    
    File synthesize(MessageModel message);

    String recognize(File audio);
}
