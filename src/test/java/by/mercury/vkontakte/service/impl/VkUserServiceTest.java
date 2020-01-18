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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VkUserServiceTest {

    private static final Integer EXISTED_ID = 1;
    private static final Integer NOT_EXISTED_ID = 0;

    @InjectMocks
    private VkUserService testedInstance;

    @Mock
    private UserDao userDao;

    @Mock
    private UserModel existedUser;

    @BeforeEach
    public void setUp() {
        when(userDao.findByPeerId(EXISTED_ID)).thenReturn(Optional.of(existedUser));
        when(userDao.findByPeerId(NOT_EXISTED_ID)).thenReturn(Optional.empty());
    }

    @Test
    public void shouldReturnExistedUserIfIdPresent() {
        Optional<UserModel> actual = testedInstance.findByPeerId(EXISTED_ID);

        Optional<UserModel> expected = Optional.of(existedUser);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyUserIfIdNotPresent() {
        Optional<UserModel> actual = testedInstance.findByPeerId(NOT_EXISTED_ID);

        Optional<UserModel> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldCallDaoMethod() {
        testedInstance.saveAll(Collections.emptyList());

        verify(userDao).saveAll(Collections.emptyList());
    }
}
