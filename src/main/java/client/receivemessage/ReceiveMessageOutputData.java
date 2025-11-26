package client.receivemessage;

import common.Attachment;

/** Info for an outbound received message.
 *  holds the info needed to make a Message
 */
public class ReceiveMessageOutputData {
    private final String sender;
    private final String content;
    private final String timestamp;
    private final Attachment attachment;

    /** Basic Constructor.
     *
     * @param sender user that sent it
     * @param content content of the message
     * @param timestamp when it was sent
     * @param attachment attachment if it exists
     */
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
