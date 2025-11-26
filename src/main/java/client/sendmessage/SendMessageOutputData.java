package client.sendmessage;

/** Info for an outbound sent message.
 *  holds the info needed to make a Message
 */
public class SendMessageOutputData {
    private final String sender;
    private final String messageContent;
    private final String timestamp;

    /** Basic Constructor.
     *
     * @param sender user that sent it
     * @param messageContent content of the message
     * @param timestamp when it was sent
     */
    public SendMessageOutputData(String sender, String messageContent, String timestamp) {
        this.sender = sender;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
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
}
