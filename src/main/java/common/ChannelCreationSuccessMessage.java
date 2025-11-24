package common;

import java.time.LocalDateTime;

public class ChannelCreationSuccessMessage implements Message {

    private final String username;
    private final String channelName;
    private final LocalDateTime timestamp;

    public ChannelCreationSuccessMessage(String username, String channelName) {
        this.username = username;
        this.channelName = channelName;
        this.timestamp = LocalDateTime.now();
    }

    public static ChannelCreationSuccessMessage deserialize(String data) {
        String[] parts = data.split("\\|", 2);
        return new ChannelCreationSuccessMessage(parts[0], parts[1]);
    }

    @Override
    public String serialize() {
        return username + "|" + channelName;
    }

    @Override public String getUsername() { return username; }
    @Override public String getContent() { return channelName; }
    @Override public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public Attachment getAttachment() {
        return null;
    }

    public String getChannelName() {
        return channelName;
    }
}
