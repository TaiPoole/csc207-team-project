package Common;

import java.time.LocalDateTime;

public class Serialized_Message implements Message_Interface{

    private String username;
    private String content;
    private LocalDateTime timestamp;

    public Serialized_Message(String serializedMessage) {
        if (serializedMessage == null || serializedMessage.isEmpty()) {
            throw new IllegalArgumentException("Serialized message cannot be null or empty");
        }

        String[] parts = serializedMessage.split("\n", 3);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid format");
        }

        this.username = parts[0];
        this.content = parts[1];
        this.timestamp = LocalDateTime.parse(parts[2]);
    }

    public String serialize() {
        return this.username + "\n" + this.content + "\n" + this.timestamp;
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
