package Common;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class attachmentMessage implements MessageInterface {
    private String username;
    private String content;
    private LocalDateTime timestamp;
    private BufferedImage attachment;

    public attachmentMessage(String username, String content, LocalDateTime timestamp, BufferedImage attachment) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.attachment = attachment;
    }

    public String serialize() {
        return this.username + "\n" + this.content + "\n" + this.timestamp + "\n" + this.attachment;
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

    public BufferedImage getAttachment() {
        return attachment;
    }

}
