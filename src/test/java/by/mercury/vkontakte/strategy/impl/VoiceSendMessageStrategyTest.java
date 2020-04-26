package by.mercury.vkontakte.strategy.impl;

import by.mercury.core.exception.SendMessageException;
import by.mercury.core.model.MessageModel;
import by.mercury.core.model.UserModel;
import by.mercury.core.service.SynthesizeSpeechService;
import com.vk.api.sdk.actions.Docs;
import com.vk.api.sdk.actions.Messages;
import com.vk.api.sdk.actions.Upload;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.UploadServer;
import com.vk.api.sdk.objects.docs.responses.DocUploadResponse;
import com.vk.api.sdk.objects.docs.responses.SaveResponse;
import com.vk.api.sdk.objects.enums.DocsType;
import com.vk.api.sdk.objects.messages.AudioMessage;
import com.vk.api.sdk.queries.docs.DocsGetMessagesUploadServerQuery;
import com.vk.api.sdk.queries.docs.DocsSaveQuery;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import com.vk.api.sdk.queries.upload.UploadDocQuery;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VoiceSendMessageStrategyTest {

    private static final Integer PEER_ID = 12345;
    private static final Integer MESSAGE_ID = 54321;
    private static final String UPLOAD_URL = "http://localhost";
    private static final String EXCEPTION_MESSAGE = "message";
    private static final String MESSAGE_TEXT = "text";

    @InjectMocks
    private VoiceSendMessageStrategy testedInstance;

    @Mock
    private SynthesizeSpeechService synthesizeSpeechService;
    @Mock
    private VkApiClient vkApiClient;
    @Mock
    private GroupActor vkGroupActor;

    @Mock
    private Docs docs;
    @Mock
    private DocsGetMessagesUploadServerQuery uploadServerQuery;
    @Mock
    private UploadServer uploadServer;
    @Mock
    private DocsSaveQuery docsSaveQuery;
    @Mock
    private SaveResponse saveResponse;
    @Mock
    private AudioMessage audioMessage;

    @Mock
    private Upload upload;
    @Mock
    private UploadDocQuery uploadDocQuery;
    @Mock
    private DocUploadResponse docUploadResponse;
    
    @Mock
    private Messages messages;
    @Mock
    private MessagesSendQuery query;
    
    @Mock
    private MessageModel message;
    @Mock
    private UserModel target;

    private File audioFile;

    private URL uploadUrl;

    @BeforeEach
    public void setUp() throws ClientException, ApiException, IOException {
        uploadUrl = URI.create(UPLOAD_URL).toURL();

        audioFile = File.createTempFile("prefix", "suffix");
        when(synthesizeSpeechService.synthesize(eq(message))).thenReturn(audioFile);
        
        when(vkApiClient.upload()).thenReturn(upload);
        when(vkApiClient.messages()).thenReturn(messages);

        when(vkApiClient.docs()).thenReturn(docs);
        when(docs.getMessagesUploadServer(eq(vkGroupActor))).thenReturn(uploadServerQuery);
        when(uploadServerQuery.peerId(eq(PEER_ID))).thenReturn(uploadServerQuery);
        when(uploadServerQuery.type(DocsType.AUDIO_MESSAGE)).thenReturn(uploadServerQuery);
        when(uploadServerQuery.execute()).thenReturn(uploadServer);
        when(uploadServer.getUploadUrl()).thenReturn(uploadUrl);

        when(docs.save(eq(vkGroupActor), eq(UPLOAD_URL))).thenReturn(docsSaveQuery);
        when(docsSaveQuery.execute()).thenReturn(saveResponse);
        when(saveResponse.getAudioMessage()).thenReturn(audioMessage);
        when(audioMessage.getId()).thenReturn(MESSAGE_ID);
        when(audioMessage.getOwnerId()).thenReturn(PEER_ID);
        
        when(upload.doc(eq(UPLOAD_URL), any())).thenReturn(uploadDocQuery);
        when(uploadDocQuery.execute()).thenReturn(docUploadResponse);
        when(docUploadResponse.getFile()).thenReturn(UPLOAD_URL);

        when(messages.send(eq(vkGroupActor))).thenReturn(query);
        when(query.peerId(anyInt())).thenReturn(query);
        when(query.attachment(anyString())).thenReturn(query);
        when(query.message(eq(StringUtils.EMPTY))).thenReturn(query);
        when(query.randomId(anyInt())).thenReturn(query);
        
        when(message.getTarget()).thenReturn(target);
        when(message.getText()).thenReturn(MESSAGE_TEXT);
        when(target.getPeerId()).thenReturn(PEER_ID);
    }

    @Test
    public void shouldSendMessageIfPresent() throws ClientException, ApiException {
        when(query.execute()).thenReturn(null);

        assertDoesNotThrow(() -> testedInstance.send(message));
    }

    @Test
    public void shouldThrowExceptionIfNotPresent() throws ClientException, ApiException {
        when(query.execute()).thenThrow(new ClientException(EXCEPTION_MESSAGE));

        SendMessageException actual = assertThrows(SendMessageException.class, () -> testedInstance.send(message));

        assertEquals(EXCEPTION_MESSAGE, actual.getMessage());
    }
}
