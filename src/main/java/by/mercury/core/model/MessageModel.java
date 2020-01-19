package by.mercury.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Describes messages
 *
 * @author Yegor Ikbaev
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {

    private String text;

    private UserModel author;

    private UserModel target;
}
