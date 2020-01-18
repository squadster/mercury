package by.mercury.vkontakte.service.impl;

import by.mercury.core.model.UserModel;
import by.mercury.core.service.LoadUsersByConversationService;
import by.mercury.core.service.UserService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.enums.MessagesFilter;
import com.vk.api.sdk.objects.messages.ConversationWithMessage;
import com.vk.api.sdk.objects.messages.responses.GetConversationsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Vkontakte implementation of {@link LoadUsersByConversationService}
 *
 * @author Yegor Ikbaev
 */
@Service
public class VkLoadUsersByConversationService implements LoadUsersByConversationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VkLoadUsersByConversationService.class);

    private static final Integer LIMIT_CONVERSATION = 200;
    private static final Integer INITIAL_OFFSET = 0;

    private UserService userService;

    private VkApiClient vkApiClient;

    private GroupActor vkGroupActor;

    private Converter<ConversationWithMessage, UserModel> converter;

    /**
     * Load all users from {@link ConversationWithMessage} and save they in repository as {@link UserModel}.
     */
    @Override
    public void load() {
        Integer offset = INITIAL_OFFSET;
        do {
            List<UserModel> loadedUsers = loadUsersFromConversations(offset);
            Optional.of(findNotExistedUsers(loadedUsers))
                    .filter(Predicate.not(List::isEmpty))
                    .ifPresent(userService::saveAll);
            offset = loadedUsers.size();
        } while (offset > INITIAL_OFFSET);
    }

    private List<UserModel> loadUsersFromConversations(Integer offset) {
        return executeRequest(offset)
                .map(GetConversationsResponse::getItems)
                .stream()
                .flatMap(Collection::stream)
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    private Optional<GetConversationsResponse> executeRequest(Integer offset) {
        GetConversationsResponse response = null;
        try {
            response = vkApiClient.messages()
                    .getConversations(vkGroupActor)
                    .filter(MessagesFilter.ALL)
                    .count(LIMIT_CONVERSATION)
                    .offset(offset)
                    .execute();
        } catch (ApiException | ClientException exception) {
            LOGGER.error(exception.getMessage());
        }
        return Optional.ofNullable(response);
    }

    /**
     * @param loadedUsers are all users from response
     * @return {@link List<UserModel>}, which are not saved in repository
     */
    private List<UserModel> findNotExistedUsers(List<UserModel> loadedUsers) {
        Set<Integer> existedUsersIds = findExistedUsersIds(loadedUsers);
        return loadedUsers.stream()
                .filter(loadedUser -> !existedUsersIds.contains(loadedUser.getPeerId()))
                .collect(Collectors.toList());
    }

    private Set<Integer> findExistedUsersIds(List<UserModel> loadedUsers) {
        return loadedUsers.stream()
                .map(UserModel::getPeerId)
                .map(userService::findByPeerId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(UserModel::getPeerId)
                .collect(Collectors.toSet());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setVkApiClient(VkApiClient vkApiClient) {
        this.vkApiClient = vkApiClient;
    }

    @Autowired
    public void setVkGroupActor(GroupActor vkGroupActor) {
        this.vkGroupActor = vkGroupActor;
    }

    @Autowired
    public void setConverter(Converter<ConversationWithMessage, UserModel> converter) {
        this.converter = converter;
    }
}
