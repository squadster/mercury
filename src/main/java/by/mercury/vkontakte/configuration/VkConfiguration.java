package by.mercury.vkontakte.configuration;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:tokens.properties")
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
}
