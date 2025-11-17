package Common;

import java.time.LocalDateTime;

public class Attachment_Message implements Message_Interface{
    private String username;
    private String content;
    private LocalDateTime timestamp;
    private Attachment attachment;

    public Attachment_Message(String username, String content, LocalDateTime timestamp, Attachment attachment) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.attachment = attachment;
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
