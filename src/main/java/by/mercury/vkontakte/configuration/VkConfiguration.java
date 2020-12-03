package by.mercury.vkontakte.configuration;

import by.mercury.vkontakte.strategy.impl.VkTextSendMessageStrategy;
import by.mercury.vkontakte.strategy.impl.VkVoiceSendMessageStrategy;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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
    public VkTextSendMessageStrategy textSendMessageStrategy() {
        return new VkTextSendMessageStrategy();
    }

    @Bean
    public VkVoiceSendMessageStrategy voiceSendMessageStrategy() {
        return new VkVoiceSendMessageStrategy();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
