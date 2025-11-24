package common;

import java.time.LocalDateTime;
import java.util.Arrays;

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
        return this.username + "\n" + this.timestamp + "\n" + this.content + "\n" + this.attachment.getName() + "\n" + Arrays.toString(this.attachment.getAttachment());
    }

    public static Message deserialize(String message) {
        return new AttachmentMessage("test", message, LocalDateTime.now(), null);
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
