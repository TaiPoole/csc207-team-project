package client.receivemessage;

import common.Attachment;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

/** Manager for incoming received messages.
 *  follows restrictions set by ReceiveMessageOutputBoundary
 *  in charge of updating the UI when a message is received
 */
public class ReceiveMessagePresenter implements ReceiveMessageOutputBoundary {
    private final DefaultListModel<String> messageModel;

    /** Basic constructor.
     *
     * @param messageModel message list to update when message is received
     */
    public ReceiveMessagePresenter(DefaultListModel<String> messageModel) {
        this.messageModel = messageModel;
    }

    @Override
    public void displayMessage(ReceiveMessageOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            // bodge; should be fixed at some point to display something other than string and check instanceof instead
            if (outputData.getAttachment() != null) {
                Attachment attachment = outputData.getAttachment();
                String fileMessage = String.format("[%s] %s: %s | %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        outputData.getContent(),
                        attachment.getName()
                );
                messageModel.addElement(fileMessage);
            } else {
                String formattedMessage = String.format("[%s] %s: %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        outputData.getContent()
                );

                messageModel.addElement(formattedMessage);

            }
        });

    }

}
