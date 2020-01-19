package by.mercury.core.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for {@link by.mercury.core.model.MessageModel}
 *
 * @author Yegor Ikbaev
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageData {

    private String text;

    private UserData author;

    private UserData target;
}
