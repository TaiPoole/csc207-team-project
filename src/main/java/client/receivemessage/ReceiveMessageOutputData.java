package client.receivemessage;

import common.Attachment;

public class ReceiveMessageOutputData {
    private String sender;
    private String content;
    private String timestamp;
    private Attachment attachment;

    public ReceiveMessageOutputData(String sender, String content, String timestamp,  Attachment attachment) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.attachment = attachment;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Attachment getAttachment() {
        return attachment;
    }
}
