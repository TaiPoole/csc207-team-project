package Common;
import java.time.LocalDateTime;

public class textMessage implements  Message {
    private String username;
    private String content;
    private LocalDateTime timestamp;

    public textMessage(String username, String content, LocalDateTime timestamp) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    public static Message deserialize(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Serialized message cannot be null or empty");
        }

        String[] parts = message.split("\n", 3);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid format");
        }

        return new textMessage(parts[0], parts[2], LocalDateTime.parse(parts[1]));
    }


    public String serialize() {
        return this.username + "\n" + this.timestamp + "\n" + this.content;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
