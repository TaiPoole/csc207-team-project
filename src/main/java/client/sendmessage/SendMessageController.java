package client.sendmessage;

import common.Attachment;

public class SendMessageController {
    private final SendMessageInputBoundary sendMessageInteractor;

    public SendMessageController(SendMessageInputBoundary sendMessageInteractor) {
        this.sendMessageInteractor = sendMessageInteractor;
    }

    public void sendMessage(String messageContent) {
        SendMessageInputData inputData = new SendMessageInputData(messageContent);
        sendMessageInteractor.execute(inputData);
    }

    public void sendMessageWithAttachment(String messageContent, Attachment attachment) {
        SendMessageInputData inputData = new SendMessageInputData(messageContent, attachment);
        sendMessageInteractor.execute(inputData);
    }
}