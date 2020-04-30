package by.mercury.core.strategy.impl;

import by.mercury.core.strategy.RecognizeSpeechStrategy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.transcribestreaming.TranscribeStreamingAsyncClient;
import software.amazon.awssdk.services.transcribestreaming.model.AudioEvent;
import software.amazon.awssdk.services.transcribestreaming.model.AudioStream;
import software.amazon.awssdk.services.transcribestreaming.model.LanguageCode;
import software.amazon.awssdk.services.transcribestreaming.model.MediaEncoding;
import software.amazon.awssdk.services.transcribestreaming.model.Result;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionRequest;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionResponseHandler;
import software.amazon.awssdk.services.transcribestreaming.model.TranscriptEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class AmazonRecognizeSpeechStrategy implements RecognizeSpeechStrategy {

    private TranscribeStreamingAsyncClient transcribeStreamingAsyncClient;

    @Override
    public String recognize(File audio) {
        try {
            var words = new HashSet<String>();
            var result = transcribeStreamingAsyncClient.startStreamTranscription(request(),
                    new AudioStreamPublisher(null, new FileInputStream(audio)),
                    getResponseHandler(words));
            result.get();
            return words.stream()
                    .max(Comparator.comparingInt(String::length))
                    .orElse(StringUtils.EMPTY);
        } catch (InterruptedException | ExecutionException | FileNotFoundException exception) {
            return StringUtils.EMPTY;
        }
    }

    private StartStreamTranscriptionRequest request() {
        return StartStreamTranscriptionRequest.builder()
                .languageCode(LanguageCode.EN_US.toString())
                .mediaEncoding(MediaEncoding.PCM)
                .mediaSampleRateHertz(16_000)
                .build();
    }

    @Autowired
    public void setTranscribeStreamingAsyncClient(TranscribeStreamingAsyncClient transcribeStreamingAsyncClient) {
        this.transcribeStreamingAsyncClient = transcribeStreamingAsyncClient;
    }

    private StartStreamTranscriptionResponseHandler getResponseHandler(Set<String> words) {
        return StartStreamTranscriptionResponseHandler.builder()
                .onResponse(response -> log.debug("Received initial response"))
                .onError(error -> log.warn("Error during recognizing: " + error.getMessage()))
                .onComplete(() -> log.debug("All records stream successfully"))
                .subscriber(event -> {
                    List<Result> results = ((TranscriptEvent) event).transcript().results();
                    if (!results.isEmpty() && !results.get(0).alternatives().get(0).transcript().isEmpty()) {
                        words.add(results.get(0).alternatives().get(0).transcript());
                    }
                })
                .build();
    }

    private static class AudioStreamPublisher implements Publisher<AudioStream> {

        private final InputStream inputStream;

        private Subscription subscription;

        private AudioStreamPublisher(Subscription subscription, InputStream inputStream) {
            this.subscription = subscription;
            this.inputStream = inputStream;
        }

        @Override
        public void subscribe(Subscriber<? super AudioStream> subscriber) {
            Optional.ofNullable(subscription)
                    .ifPresent(Subscription::cancel);
            subscription = new SubscriptionImpl(subscriber, inputStream);
            subscriber.onSubscribe(subscription);
        }
    }

    public static class SubscriptionImpl implements Subscription {
        private static final int CHUNK_SIZE_IN_BYTES = 1024 * 8;
        private final Subscriber<? super AudioStream> subscriber;
        private final InputStream inputStream;
        private final ExecutorService executor = Executors.newFixedThreadPool(1);
        private final AtomicLong demand = new AtomicLong(0);

        private SubscriptionImpl(Subscriber<? super AudioStream> s, InputStream inputStream) {
            this.subscriber = s;
            this.inputStream = inputStream;
        }

        @Override
        public void request(long n) {
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException("Demand must be positive"));
            }
            demand.getAndAdd(n);
            executor.submit(() -> {
                try {
                    do {
                        ByteBuffer audioBuffer = getNextEvent();
                        if (audioBuffer.remaining() > 0) {
                            AudioEvent audioEvent = audioEventFromBuffer(audioBuffer);
                            subscriber.onNext(audioEvent);
                        } else {
                            subscriber.onComplete();
                            break;
                        }
                    } while (demand.decrementAndGet() > 0);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            });
        }

        @Override
        public void cancel() {
            executor.shutdown();
        }

        private ByteBuffer getNextEvent() {
            ByteBuffer audioBuffer = null;
            int len = 0;
            try {
                byte[] audioBytes = new byte[CHUNK_SIZE_IN_BYTES];
                len = inputStream.read(audioBytes);
                if (len <= 0) {
                    audioBuffer = ByteBuffer.allocate(0);
                } else {
                    audioBuffer = ByteBuffer.wrap(audioBytes, 0, len);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            return audioBuffer;
        }

        private AudioEvent audioEventFromBuffer(ByteBuffer bb) {
            return AudioEvent.builder()
                    .audioChunk(SdkBytes.fromByteBuffer(bb))
                    .build();
        }
    }
}
