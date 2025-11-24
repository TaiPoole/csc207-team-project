package common;

import java.time.LocalDateTime;
import java.util.Base64;

/**
 * A message with an attached file; file is base64 encoded.
 */
public class AttachmentMessage implements Message {
    private final String username;
    private final String content;
    private final LocalDateTime timestamp;
    private final Attachment attachment;

    public AttachmentMessage(String username, String content, LocalDateTime timestamp, Attachment attachment) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.attachment = attachment;
    }

    public String serialize() {
        String encodedBytes = Base64.getEncoder().encodeToString(this.attachment.getAttachment());
        return this.username + "\n"
                + this.timestamp + "\n"
                + this.content + "\n"
                + this.attachment.getName() + "\n"
                + encodedBytes;
    }

    public static Message deserialize(String message) {
        String[] parts = message.split("\n", 5);

        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid AttachmentMessage format");
        }

        String username = parts[0];
        LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
        String content = parts[2];
        String fileName = parts[3];

        byte[] fileBytes = Base64.getDecoder().decode(parts[4]);
        Attachment attachment = new Attachment(fileName, fileBytes);

        return new AttachmentMessage(username, content, timestamp, attachment);
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