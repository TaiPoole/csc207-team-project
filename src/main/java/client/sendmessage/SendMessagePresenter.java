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

            if (outputData.getAttachment() != null) {
                Attachment attachment = outputData.getAttachment();
                String fileMessage = String.format("[%s] %s: %s | %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        outputData.getMessageContent(),
                        attachment.getName()
                );
                messageModel.addElement(fileMessage);


            } else {
                String formattedMessage = String.format("[%s] %s: %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        outputData.getMessageContent()
                );

                messageModel.addElement(formattedMessage);
            }
        });
    }

    @Override
    public void prepareFailureView(String error) {
        SwingUtilities.invokeLater(() -> messageModel.addElement("ERROR: " + error));
    }
}
