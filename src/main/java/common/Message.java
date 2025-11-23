package common;

import java.time.LocalDateTime;

public interface Message {
    String serialize();

    static Message deserialize(String message) {
        return null;
    }

    String getUsername();

    String getContent();

    LocalDateTime getTimestamp();

    Attachment getAttachment();
}