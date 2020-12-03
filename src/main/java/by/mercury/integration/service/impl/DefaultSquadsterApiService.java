package by.mercury.integration.service.impl;

import by.mercury.core.model.UserModel;
import by.mercury.integration.data.QueueNumberData;
import by.mercury.integration.service.SquadsterApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DefaultSquadsterApiService implements SquadsterApiService {
    
    private RestTemplate restTemplate;

    private String queueRequestUrl;

    public DefaultSquadsterApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public QueueNumberData getQueueData(UserModel user) {
        return restTemplate.getForObject(queueRequestUrl + user.getPeerId(), QueueNumberData.class);
    }

    @Value("${api.url}")
    public void setQueueRequestUrl(String queueRequestUrl) {
        this.queueRequestUrl = queueRequestUrl;
    }
}
