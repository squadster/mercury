package by.mercury.vkontakte.service.impl;

import by.mercury.core.dao.UserDao;
import by.mercury.core.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VkUserServiceTest {

    private static final Long EXISTED_USER_ID = 1L;
    private static final Long NOT_EXISTED_USER_ID = 0L;
    private static final Integer EXISTED_PEER_ID = 1;
    private static final Integer NOT_EXISTED_PEER_ID = 0;

    @InjectMocks
    private VkUserService testedInstance;

    @Mock
    private UserDao userDao;

    @Mock
    private UserModel existedUser;

    @BeforeEach
    public void setUp() {
        when(userDao.findByUid(eq(EXISTED_PEER_ID.toString()))).thenReturn(Optional.of(existedUser));
        when(userDao.findByUid(eq(NOT_EXISTED_PEER_ID.toString()))).thenReturn(Optional.empty());
        when(userDao.findById(EXISTED_USER_ID)).thenReturn(Optional.of(existedUser));
        when(userDao.findById(NOT_EXISTED_USER_ID)).thenReturn(Optional.empty());
    }

    @Test
    public void shouldReturnExistedUserIfUserIdPresent() {
        Optional<UserModel> actual = testedInstance.findById(EXISTED_USER_ID);

        Optional<UserModel> expected = Optional.of(existedUser);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyUserIfUserIdNotPresent() {
        Optional<UserModel> actual = testedInstance.findById(NOT_EXISTED_USER_ID);

        Optional<UserModel> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnExistedUserIfPeerIdPresent() {
        Optional<UserModel> actual = testedInstance.findByPeerId(EXISTED_PEER_ID);

        Optional<UserModel> expected = Optional.of(existedUser);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyUserIfPeerIdNotPresent() {
        Optional<UserModel> actual = testedInstance.findByPeerId(NOT_EXISTED_PEER_ID);

        Optional<UserModel> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldCallSaveAllDaoMethod() {
        testedInstance.saveAll(Collections.singleton(existedUser));

        verify(userDao).saveAll(Collections.singleton(existedUser));
    }

    @Test
    public void shouldCallSaveDaoMethod() {
        testedInstance.save(existedUser);

        verify(userDao).save(eq(existedUser));
    }
}
