package by.mercury.core.service.impl;

import by.mercury.core.dao.UserDao;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.UserService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Implementation of {@link UserService}
 *
 * @author Yegor Ikbaev
 */
@Service
public class DefaultUserService implements UserService {

    private UserDao userDao;

    @Override
    public Optional<UserModel> findById(Long userId) {
        return userDao.findById(userId);
    }

    @Override
    public Optional<UserModel> findByPeerId(Integer peerId) {
        return userDao.findByUid(peerId.toString());
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
