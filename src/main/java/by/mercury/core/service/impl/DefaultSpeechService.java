package by.mercury.core.service.impl;

import by.mercury.core.model.MessageModel;
import by.mercury.core.service.SpeechService;
import by.mercury.core.strategy.RecognizeSpeechStrategy;
import by.mercury.core.strategy.SynthesizeSpeechStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Default implementation of {@link SpeechService}.
 *
 * @author Yegor Ikbaev
 */
@Slf4j
public class DefaultSpeechService implements SpeechService {

    private SynthesizeSpeechStrategy synthesizeSpeechStrategy;

    private RecognizeSpeechStrategy recognizeSpeechStrategy;

    @Override
    public File synthesize(MessageModel message) {
        return synthesizeSpeechStrategy.synthesize(message);
    }
    
    @Override
    public String recognize(File audio) {
        var wav = encodeToWav(audio);
        var text = recognizeSpeechStrategy.recognize(wav);
        try {
            Files.deleteIfExists(wav.toPath());
            log.info("File {} is deleted", wav.getName());
        } catch (IOException exception) {
            log.warn("Error during deleting {}", wav.getName());
        }
        return text;
    }

    private File encodeToWav(File mp3) {
        try {
            var outputFileName = mp3.getAbsolutePath().replace("mp3", "wav");
            var process = new ProcessBuilder("ffmpeg", "-i", mp3.getAbsolutePath(), "-ar", "16000", "-ac", "1", outputFileName);
            process.redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT);
            process.start().waitFor();
            Files.deleteIfExists(mp3.toPath());
            log.info("File {} is deleted and converted to {}", mp3.getName(), outputFileName);
            return new File(outputFileName);
        } catch (InterruptedException | IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Autowired
    public void setSynthesizeSpeechStrategy(SynthesizeSpeechStrategy synthesizeSpeechStrategy) {
        this.synthesizeSpeechStrategy = synthesizeSpeechStrategy;
    }

    @Autowired(required = false)
    public void setRecognizeSpeechStrategy(RecognizeSpeechStrategy recognizeSpeechStrategy) {
        this.recognizeSpeechStrategy = recognizeSpeechStrategy;
    }
}
