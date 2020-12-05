package by.mercury.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@Entity
@Table(name = "user_configurations")
public class UserConfigurationModel {

    @Id
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    private String speaker;
    
    private String language;
    
    private String rate;

    @Column(name = "enable_voice_messages")
    private Boolean enableVoiceMessages;
}
