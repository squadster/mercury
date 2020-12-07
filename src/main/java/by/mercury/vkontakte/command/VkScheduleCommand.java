package by.mercury.vkontakte.command;

import by.mercury.core.command.CommandContext;
import by.mercury.core.data.MessageType;
import by.mercury.core.model.MessageModel;
import by.mercury.core.service.ScheduleService;
import by.mercury.core.service.UploadService;
import com.vk.api.sdk.objects.enums.DocsType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
public class VkScheduleCommand extends AbstractVkCommand {
    
    private final ScheduleService scheduleService;
    
    private final UploadService uploadService;
    
    public VkScheduleCommand(ScheduleService scheduleService, UploadService uploadService) {
        super(Arrays.asList("расписание", "schedule", "timetable", "table"));
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
                .ifPresentOrElse(file -> uploadService.uploadFile(messageOnSuccess(context), file, DocsType.DOC), 
                        () -> getMessageService().send(messageOnFailure(context)));
        log.info("Schedule receiving command for VK was successfully completed");
    }
    
    private MessageModel messageOnSuccess(CommandContext context) {
        return MessageModel.builder()
                .target(context.getMessage().getAuthor())
                .types(Collections.singletonList(MessageType.TEXT))
                .text("")
                .build();
    }
    
    private MessageModel messageOnFailure(CommandContext context) {
        return MessageModel.builder()
                .target(context.getMessage().getAuthor())
                .types(Collections.singletonList(MessageType.TEXT))
                .text("Расписание не найдено")
                .build();
    }
}
