package by.mercury.core.data;

import java.util.Arrays;
import java.util.Optional;

/**
 * Type of message
 *
 * @author Yegor Ikbaev
 */
public enum MessageType {
    TEXT("text"),
    VOICE("voice");
    
    private String code;
    
    MessageType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
    public static MessageType toMessageType(String code) {
        return Optional.ofNullable(code)
                .map(MessageType::resolve)
                .orElse(TEXT);
    }
    
    private static MessageType resolve(String code) {
        return Arrays.stream(values())
                .filter(type -> type.getCode().toLowerCase().equals(code.toLowerCase()))
                .findFirst()
                .orElse(TEXT);
    }
}
