package client.sendmessage;

import common.Attachment;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

/** Manager for send message outputs.
 *  follows the restrictions set by SendMessageOutputBoundary
 *  in charge of updating the UI when a message is sent
 */
public class SendMessagePresenter implements SendMessageOutputBoundary {
    private final DefaultListModel<String> messageModel;

    /** Basic constructor.
     *
     * @param messageModel message list to update when message is sent
     */
    public SendMessagePresenter(DefaultListModel<String> messageModel) {
        this.messageModel = messageModel;
    }

    @Override
    public void prepareSuccessView(SendMessageOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            Attachment attachment = outputData.getAttachment();

            String formattedMessage;
            if (attachment != null) {
                // Show file name in chat when sending with attachment
                formattedMessage = String.format("[%s] %s: %s | %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        outputData.getMessageContent(),
                        attachment.getName()
                );
            } else {
                // Text-only message
                formattedMessage = String.format("[%s] %s: %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        outputData.getMessageContent()
                );
            }

            messageModel.addElement(formattedMessage);
        });
    }

    @Override
    public void prepareFailureView(String error) {
        SwingUtilities.invokeLater(() -> messageModel.addElement("ERROR: " + error));
    }
}
