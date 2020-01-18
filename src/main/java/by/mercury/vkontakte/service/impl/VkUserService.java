package by.mercury.vkontakte.service.impl;

import by.mercury.core.dao.UserDao;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Vkontakte implementation of {@link UserService}
 *
 * @author Yegor Ikbaev
 */
@Service
public class VkUserService implements UserService {

    private UserDao userDao;

    @Override
    public Optional<UserModel> findByPeerId(Integer peerId) {
        return userDao.findByPeerId(peerId);
    }

    @Override
    public void saveAll(Collection<UserModel> users) {
        userDao.saveAll(users);
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
