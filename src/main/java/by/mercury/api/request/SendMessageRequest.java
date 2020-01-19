package by.mercury.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describes information of request in {@link by.mercury.api.controller.SendMessageRestController}
 *
 * @author Yegor Ikbaev
 */
@Data
@NoArgsConstructor
public class SendMessageRequest {

    private String text;

    private Long target;
}
