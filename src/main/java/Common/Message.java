package Common;
import java.time.LocalDateTime;

public class Message {
    private String username;
    private String content;
    private LocalDateTime timestamp;
    private Attachment attachment;

    public Message(String username, String content, LocalDateTime timestamp) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Message(String username, String content, LocalDateTime timestamp, Attachment attachment) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.attachment = attachment;
    }

    public Message(String serializedMessage) {
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

    public Attachment getAttachment() {
        return attachment;
    }

}
