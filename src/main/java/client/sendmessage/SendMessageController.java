package client.sendmessage;

import common.Attachment;

/** SendMessageController class.
 *  holds all the logic for interacting/input/output, wraps it for general calling
 */
public class SendMessageController {
    private final SendMessageInputBoundary sendMessageInteractor;

    /** Basic Constructor.
     *
     * @param sendMessageInteractor interactor for handling of SendMessageInputData's
     */
    public SendMessageController(SendMessageInputBoundary sendMessageInteractor) {
        this.sendMessageInteractor = sendMessageInteractor;
    }

    /** Sends a message.
     *  given provided input, converts it into SendMessageInputData format and has it handled by the interactor
     *
     * @param messageContent raw input from user
     */
    public void sendMessage(String messageContent) {
        SendMessageInputData inputData = new SendMessageInputData(messageContent);
        sendMessageInteractor.execute(inputData);
    }

    /** Sends a message with an attachment.
     *  similar to above, just with an attachment as well
     *
     * @param messageContent raw text input from user
     * @param attachment file attached to message by user
     */
    public void sendMessageWithAttachment(String messageContent, Attachment attachment) {
        SendMessageInputData inputData = new SendMessageInputData(messageContent, attachment);
        sendMessageInteractor.execute(inputData);
    }
}