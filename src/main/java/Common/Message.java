package Common;

import Server.User;

import java.util.Date;

public class Message {
    private final User user;
    private String content;
    private Date timestamp;
    private Attachment attachment;

    public Message(User user, String content, Date timestamp) {
        this.user = user;
        this.content = content;
        this.timestamp = timestamp;
    }
    public Message(User user, String content, Date timestamp, Attachment attachment) {
        this.user = user;
        this.content = content;
        this.timestamp = timestamp;
        this.attachment = attachment;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Attachment getAttachment() {
        return attachment;
    }

}
