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
public class UserDataConverterTest {

    private static final Long ID = 0L;
    private static final Integer PEER_ID = 0;
    private static final Long USER_ID = 0L;

    private UserDataConverter testedInstance;

    @Mock
    private UserData source;

    @BeforeEach
    public void setUp() {
        testedInstance = new UserDataConverter();
        when(source.getId()).thenReturn(ID);
        when(source.getPeerId()).thenReturn(PEER_ID);
    }

    @Test
    public void shouldReturnMessageModelIfPresent() {
        UserModel actual = testedInstance.convert(source);

        assertEquals(ID, actual.getId());
        assertEquals(PEER_ID, actual.getPeerId());
    }
}
