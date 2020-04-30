package by.mercury.vkontakte.configuration;

import by.mercury.core.data.MessageType;
import by.mercury.core.strategy.SendMessageStrategy;
import by.mercury.vkontakte.strategy.impl.TextSendMessageStrategy;
import by.mercury.vkontakte.strategy.impl.VoiceSendMessageStrategy;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
public class VkConfiguration {

    @Value("${vk.group.id}")
    private Integer groupId;

    @Value("${vk.access.token}")
    private String accessToken;

    @Bean
    public VkApiClient vkApiClient() {
        return new VkApiClient(HttpTransportClient.getInstance());
    }

    @Bean
    public GroupActor vkGroupActor() {
        return new GroupActor(groupId, accessToken);
    }

    @Bean
    public Map<MessageType, SendMessageStrategy> sendMessageStrategies() {
        return Map.of(MessageType.TEXT, textSendMessageStrategy(), MessageType.VOICE, voiceSendMessageStrategy());
    }

    @Bean
    public TextSendMessageStrategy textSendMessageStrategy() {
        return new TextSendMessageStrategy();
    }

    @Bean
    public VoiceSendMessageStrategy voiceSendMessageStrategy() {
        return new VoiceSendMessageStrategy();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
