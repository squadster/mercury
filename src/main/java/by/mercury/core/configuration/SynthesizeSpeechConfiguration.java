package by.mercury.core.configuration;

import by.mercury.core.strategy.impl.AmazonPollySynthesizeSpeechStrategy;
import by.mercury.core.strategy.impl.LocalMarySynthesizeSpeechStrategy;
import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;

@Configuration
public class SynthesizeSpeechConfiguration {
    
    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretAccessKey}")
    private String awsSecretAccessKey;

    @Bean
    @Profile({"local", "dev"})
    public LocalMaryInterface localMaryInterface() throws MaryConfigurationException {
        return new LocalMaryInterface();
    }

    @Bean
    @Profile({"local", "dev"})
    public LocalMarySynthesizeSpeechStrategy localMarySynthesizeSpeechStrategy() {
        return new LocalMarySynthesizeSpeechStrategy();
    }

    @Bean
    @Profile({"release"})
    public AmazonPollySynthesizeSpeechStrategy amazonPollySynthesizeSpeechStrategy() {
        return new AmazonPollySynthesizeSpeechStrategy();
    }

    @Bean
    @Profile({"release"})
    public PollyClient pollyClient() {
        System.setProperty("aws.accessKeyId", awsAccessKeyId);
        System.setProperty("aws.secretAccessKey", awsSecretAccessKey);
        return PollyClient.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(SystemPropertyCredentialsProvider.create())
                .build();
    }
}
