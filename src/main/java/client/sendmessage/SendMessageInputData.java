package client.sendmessage;

import common.Attachment;

/** SendMessageInputData class.
 *  handles the inputs for an outbound message
 */
public class SendMessageInputData {

    private final String messageContent;
    private final Attachment attachment;

    /** Basic Constructor for a future AttachmentMessage.
     *
     * @param messageContent content of the message (text)
     * @param attachment attachment included with the message
     */
    public SendMessageInputData(String messageContent, Attachment attachment) {
        this.messageContent = messageContent;
        this.attachment = attachment;
    }

    /** Basic Constructor for a future TextMessage.
     *
     * @param messageContent content of the message
     */
    public SendMessageInputData(String messageContent) {
        this.messageContent = messageContent;
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

}
