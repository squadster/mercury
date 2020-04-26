package by.mercury.core.strategy.impl;

import by.mercury.core.model.MessageModel;
import marytts.LocalMaryInterface;
import marytts.exceptions.SynthesisException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

import static marytts.util.data.audio.MaryAudioUtils.getSamplesAsDoubleArray;
import static marytts.util.data.audio.MaryAudioUtils.writeWavFile;

public class LocalMarySynthesizeSpeechStrategy extends AbstractSynthesizeSpeechStrategy {

    private static final String VOICE_NAME = "cmu-slt-hsmm";
    private static final String AUDIO_MESSAGE_FILE_EXTENSION = ".wav";

    private LocalMaryInterface localMaryInterface;

    @Override
    public File synthesize(MessageModel message) {
        try {
            localMaryInterface.setVoice(VOICE_NAME);
            var audio = localMaryInterface.generateAudio(message.getText());
            var tempFile = createTempFile(message);
            writeWavFile(getSamplesAsDoubleArray(audio), tempFile.getAbsolutePath(), audio.getFormat());
            return tempFile;
        } catch (SynthesisException | IOException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    protected String getFileExtension() {
        return AUDIO_MESSAGE_FILE_EXTENSION;
    }

    @Autowired
    public void setLocalMaryInterface(LocalMaryInterface localMaryInterface) {
        this.localMaryInterface = localMaryInterface;
    }
}
