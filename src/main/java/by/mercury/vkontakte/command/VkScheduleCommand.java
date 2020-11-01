package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.MessageService;
import by.mercury.core.service.ScheduleService;
import by.mercury.vkontakte.service.UploadService;
import com.vk.api.sdk.objects.enums.DocsType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class VkScheduleCommand extends AbstractVkCommand {
    
    private ScheduleService scheduleService;
    
    private UploadService uploadService;
    
    public VkScheduleCommand(MessageService messageService, RestTemplate restTemplate, 
                             ScheduleService scheduleService, UploadService uploadService) {
        super(messageService, restTemplate, Arrays.asList("schedule", "timetable", "table", "timing", "syllabus", "sked"));
        this.scheduleService = scheduleService;
        this.uploadService = uploadService;
    }

    @Override
    public void execute(CommandContext context) {
        Optional.of(context)
                .map(CommandContext::getMessage)
                .map(MessageModel::getAuthor)
                .map(scheduleService::getScheduleForUser)
                .map(scheduleService::generateSchedule)
                .ifPresentOrElse(file -> uploadService.uploadFile(context.getMessage(), file, DocsType.DOC), 
                        () -> getMessageService().send(messageOnFailure(context)));
    }
    
    private MessageModel messageOnFailure(CommandContext context) {
        return MessageModel.builder()
                .target(context.getMessage().getAuthor())
                .types(Arrays.asList(MessageType.TEXT, MessageType.VOICE))
                .text("Не удалось получить расписание")
                .build();
    }
}
