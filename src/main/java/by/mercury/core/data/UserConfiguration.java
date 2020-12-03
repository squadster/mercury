package by.mercury.core.data;

import by.mercury.core.model.UserConfigurationModel;
import by.mercury.core.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.polly.model.VoiceId;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class UserConfiguration {

    private Long id;
    
    private UserModel user;

    private VoiceId speaker;

    private Locale language;
    
    private String rate;

    private Boolean enableVoiceMessages;
    
    private UserConfigurationModel source;
}
