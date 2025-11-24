package common;

import java.time.LocalDateTime;

public class ChannelCreationErrorMessage implements Message {

    private final String username;
    private final String reason;
    private final LocalDateTime timestamp;

    public ChannelCreationErrorMessage(String username, String reason) {
        this.username = username;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    public static ChannelCreationErrorMessage deserialize(String data) {
        String[] parts = data.split("\\|", 2);
        return new ChannelCreationErrorMessage(parts[0], parts[1]);
    }

    @Override
    public String serialize() {
        return username + "|" + reason;
    }

    @Override public String getUsername() { return username; }
    @Override public String getContent() { return reason; }
    @Override public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public Attachment getAttachment() {
        return null;
    }
}
