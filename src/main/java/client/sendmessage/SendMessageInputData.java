package client.sendmessage;

import common.Attachment;
import common.Channel;

/** SendMessageInputData class.
 *  handles the inputs for an outbound message
 */
public class SendMessageInputData {

    private final String messageContent;
    private final Attachment attachment;
    private final Channel channel;

    /** Basic Constructor for a future AttachmentMessage.
     *
     * @param messageContent content of the message (text)
     * @param attachment attachment included with the message
     */
    public SendMessageInputData(String messageContent, Attachment attachment, Channel channel) {
        this.messageContent = messageContent;
        this.attachment = attachment;
        this.channel = channel;
    }

    /** Basic Constructor for a future TextMessage.
     *
     * @param messageContent content of the message
     */
    public SendMessageInputData(String messageContent, Channel channel) {
        this.messageContent = messageContent;
        this.channel = channel;
        this.attachment = null;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    /** Checks to see if instance has attachment.
     *
     * @return True if attachment exists, False otherwise
     */
    public boolean hasAttachment() {
        return this.getAttachment() != null;
    }

    public Channel getChannel() {
        return this.channel;
    }
}
