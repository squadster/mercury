package by.mercury.vkontakte.service.impl;

import by.mercury.core.dao.UserDao;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Vk implementation of {@link UserService}
 *
 * @author Yegor Ikbaev
 */
@Service
public class VkUserService implements UserService {

    private UserDao userDao;

    @Override
    public Optional<UserModel> findByUserId(Long userId) {
        return userDao.findByUserId(userId);
    }

    @Override
    public Optional<UserModel> findByPeerId(Integer peerId) {
        return userDao.findByPeerId(peerId);
    }

    @Override
    public Collection<UserModel> saveAll(Collection<UserModel> users) {
        return IterableUtils.toList(userDao.saveAll(users));
    }

    @Override
    public UserModel save(UserModel user) {
        return userDao.save(user);
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
