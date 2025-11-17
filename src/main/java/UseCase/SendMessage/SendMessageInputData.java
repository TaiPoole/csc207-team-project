package UseCase.SendMessage;

import Common.Attachment;

public class SendMessageInputData {

    private final String messageContent;
    //private final String channelId;
    private final Attachment attachment;

    public SendMessageInputData(String messageContent, Attachment attachment) {
        this.messageContent = messageContent;
        //this.channelId = channelId;
        this.attachment = null;
    }

    public SendMessageInputData(String messageContent) {
        this.messageContent = messageContent;
        this.attachment = null;
    }

    public String  getMessageContent() {
        return messageContent;
    }
    //public String getChannelId() {
    //      return channelId;
    //}
    public Attachment getAttachment() {
        return attachment;
    }
    public boolean hasAttachment() {
        return this.getAttachment() != null;
    }

}
