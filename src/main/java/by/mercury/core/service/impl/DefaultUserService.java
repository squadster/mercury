package by.mercury.core.service.impl;

import by.mercury.core.dao.UserDao;
import by.mercury.core.model.Channel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserService}
 *
 * @author Yegor Ikbaev
 */
@Service
public class DefaultUserService implements UserService {

    private final UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<UserModel> findById(Long userId) {
        return userDao.findById(userId);
    }

    @Override
    public Optional<UserModel> findByPeerId(Integer peerId) {
        return userDao.findByUid(peerId.toString());
    }

    @Override
    public Optional<UserModel> findByTelegramId(Integer telegramId) {
        return userDao.findByTelegramId(telegramId);
    }

    @Override
    public Optional<UserModel> findByTelegramToken(String token) {
        return userDao.findByTelegramToken(token);
    }

    @Override
    public Collection<UserModel> saveAll(Collection<UserModel> users) {
        return IterableUtils.toList(userDao.saveAll(users));
    }

    @Override
    public UserModel save(UserModel user) {
        return userDao.save(user);
    }

    @Override
    public Collection<Channel> getAvailableChannels(UserModel user, Collection<Channel> channels) {
        var availableChannels = new HashSet<Channel>();
        if (user.getEnableNotificationsTelegram()) {
            availableChannels.add(Channel.TELEGRAM);
        }
        if (user.getEnableNotificationsVk()) {
            availableChannels.add(Channel.VK);
        }
        return channels.stream()
                .filter(availableChannels::contains)
                .collect(Collectors.toSet());
    }
}
