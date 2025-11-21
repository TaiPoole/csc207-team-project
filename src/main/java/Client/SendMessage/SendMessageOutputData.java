package Client.SendMessage;

import java.time.LocalDateTime;

public class SendMessageOutputData {
    private final String sender;
    private final String messageContent;
    private final String timestamp;

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
