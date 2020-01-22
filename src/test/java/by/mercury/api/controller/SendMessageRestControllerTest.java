package by.mercury.api.controller;

import by.mercury.api.converter.SendMessageRequestConverter;
import by.mercury.api.request.SendMessageRequest;
import by.mercury.api.response.SendMessageResponse;
import by.mercury.core.data.MessageData;
import by.mercury.core.exception.SendMessageException;
import by.mercury.core.facade.MessageFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SendMessageRestControllerTest {

    @InjectMocks
    private SendMessageRestController testedInstance;

    @Mock
    private MessageFacade messageFacade;

    @Mock
    private Validator validator;

    @Mock
    private SendMessageRequestConverter converter;

    @Mock
    private Errors errors;

    @Mock
    private SendMessageRequest request;

    @Mock
    private MessageData messageData;

    @BeforeEach
    public void setUp() {
        when(errors.getFieldErrors()).thenReturn(Collections.emptyList());
        when(converter.convert(request)).thenReturn(messageData);
    }

    @Test
    public void shouldReturnSuccessfulStatusIfPresent() {
        when(errors.hasErrors()).thenReturn(Boolean.FALSE);

        ResponseEntity<SendMessageResponse> actual = testedInstance.send(request, errors);

        assertTrue(actual.getBody().getSuccessful());
    }

    @Test
    public void shouldReturnFailedStatusIfNotPresent() {
        when(errors.hasErrors()).thenReturn(Boolean.TRUE);

        ResponseEntity<SendMessageResponse> actual = testedInstance.send(request, errors);

        assertFalse(actual.getBody().getSuccessful());
    }

    @Test
    public void shouldReturnFailedStatusIfException() {
        when(errors.hasErrors()).thenReturn(Boolean.FALSE);
        doThrow(SendMessageException.class).when(messageFacade).send(eq(messageData));

        ResponseEntity<SendMessageResponse> actual = testedInstance.send(request, errors);

        assertFalse(actual.getBody().getSuccessful());
    }
}
