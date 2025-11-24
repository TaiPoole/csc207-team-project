package common;

import java.time.LocalDateTime;

public abstract class CreateChannelMessage implements Message {

    private final String username;
    private final String channelName;
    private final LocalDateTime timestamp;

    public CreateChannelMessage(String username, String channelName) {
        this.username = username;
        this.channelName = channelName;
        this.timestamp = LocalDateTime.now();
    }

    public CreateChannelMessage(String username, String channelName, LocalDateTime timestamp) {
        this.username = username;
        this.channelName = channelName;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getChannelName() {
        return channelName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String getContent() {
        return channelName;
    }

    @Override
    public String serialize() {
        return username + "|" + channelName + "|" + timestamp.toString();
    }

    public static CreateChannelMessage deserialize(String data) {
        String[] parts = data.split("\\|", 3);
        String user = parts[0];
        String name = parts[1];
        LocalDateTime time = LocalDateTime.parse(parts[2]);
        return new CreateChannelMessage(user, name, time) {
            @Override
            public Attachment getAttachment() {
                return null;
            }
        };
    }
}
