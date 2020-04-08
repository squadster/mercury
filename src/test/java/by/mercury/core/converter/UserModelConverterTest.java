package by.mercury.core.converter;

import by.mercury.core.data.UserData;
import by.mercury.core.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserModelConverterTest {

    private static final Long ID = 0L;
    private static final Integer PEER_ID = 0;
    private static final Long USER_ID = 0L;

    private UserModelConverter testedInstance;

    @Mock
    private UserModel source;

    @BeforeEach
    public void setUp() {
        testedInstance = new UserModelConverter();
        when(source.getId()).thenReturn(ID);
        when(source.getPeerId()).thenReturn(PEER_ID);
    }

    @Test
    public void shouldReturnMessageModelIfPresent() {
        UserData actual = testedInstance.convert(source);

        assertEquals(ID, actual.getId());
        assertEquals(PEER_ID, actual.getPeerId());
    }
}
