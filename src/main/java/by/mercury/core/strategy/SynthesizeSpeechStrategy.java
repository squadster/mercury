package by.mercury.core.strategy;

import by.mercury.core.model.MessageModel;

import java.io.File;

/**
 * Service for synthesize voice messages.
 *
 * @author Yegor Ikbaev
 */
public interface SynthesizeSpeechStrategy {

    File synthesize(MessageModel message);

    File synthesize(String text, String voiceId);
}
