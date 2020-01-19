package by.mercury.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SendMessageException extends RuntimeException {

    public SendMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
