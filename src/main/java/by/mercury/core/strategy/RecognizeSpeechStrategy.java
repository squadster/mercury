package by.mercury.core.strategy;

import java.io.File;

/**
 * Service for recognizing voice messages.
 *
 * @author Yegor Ikbaev
 */
public interface RecognizeSpeechStrategy {

    String recognize(File audio);
}
