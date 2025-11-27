package client.sendmessage;

import common.Attachment;

/** Info for an outbound sent message.
 *  holds the info needed to make a Message
 */
public class SendMessageOutputData {
    private final String sender;
    private final String messageContent;
    private final String timestamp;
    private final Attachment attachment;


    /** Basic Constructor.
     *
     * @param sender user that sent it
     * @param messageContent content of the message
     * @param timestamp when it was sent
     */
    public SendMessageOutputData(String sender, String messageContent, String timestamp, Attachment attachment) {
        this.sender = sender;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.attachment = attachment;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Attachment getAttachment() {
        return attachment;
    }
}
