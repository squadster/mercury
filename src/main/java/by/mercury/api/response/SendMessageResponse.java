package by.mercury.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describes information of response in {@link by.mercury.api.controller.SendMessageRestController}
 *
 * @author Yegor Ikbaev
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageResponse {

    private Boolean successful;

    private String response;
}
