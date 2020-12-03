package by.mercury.core.service.impl;

import by.mercury.core.dao.UserConfigurationDao;
import by.mercury.core.dao.UserDao;
import by.mercury.core.data.UserConfiguration;
import by.mercury.core.model.Channel;
import by.mercury.core.model.UserConfigurationModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.polly.model.VoiceId;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
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

    private final UserConfigurationDao userConfigurationDao;

    public DefaultUserService(UserDao userDao, UserConfigurationDao userConfigurationDao) {
        this.userDao = userDao;
        this.userConfigurationDao = userConfigurationDao;
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
        if (user.getId() != null) {
            return userDao.save(user);
        } else {
            return user;
        }
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

    @Override
    public UserConfiguration getUserConfigurationForUser(UserModel user) {
        return userConfigurationDao.findByUserId(user.getId())
                .map(model -> convert(model, user))
                .orElseGet(() -> getDefaultUserConfigurationForUser(user));
    }

    @Override
    public UserConfiguration getDefaultUserConfigurationForUser(UserModel user) {
        var configurations = UserConfigurationModel.builder()
                .userId(user.getId())
                .language(Locale.ENGLISH.getLanguage())
                .speaker("Maxim")
                .enableVoiceMessages(true)
                .rate("medium")
                .build();
        if (user.getId() != null) {
            configurations = userConfigurationDao.save(configurations);
        }
        return convert(configurations, user);
    }
    
    private UserConfiguration convert(UserConfigurationModel model, UserModel user) {
        return UserConfiguration.builder()
                .id(model.getId())
                .user(user)
                .speaker(VoiceId.valueOf(model.getSpeaker().toUpperCase()))
                .language(Locale.forLanguageTag(model.getLanguage()))
                .enableVoiceMessages(model.getEnableVoiceMessages())
                .rate(model.getRate())
                .source(model)
                .build();
    }

    @Override
    public void update(UserConfigurationModel configurations) {
        if (configurations.getId() != null) {
            userConfigurationDao.save(configurations);
        }
    }
}
