package by.mercury.integration.service;

import by.mercury.core.model.UserModel;
import by.mercury.integration.data.QueueNumberData;

public interface SquadsterApiService {

    QueueNumberData getQueueData(UserModel user);
}
