package by.mercury.vkontakte.service;

import by.mercury.core.model.MessageModel;
import com.vk.api.sdk.objects.enums.DocsType;

import java.io.File;

public interface UploadService {

    String uploadFile(MessageModel message, File voiceMessage, DocsType type);
}
