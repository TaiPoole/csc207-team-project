package client.receivemessage;

import common.Attachment;
import interfaceadapter.AttachmentRegistry;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

/** Manager for incoming received messages.
 *  follows restrictions set by ReceiveMessageOutputBoundary
 *  in charge of updating the UI when a message is received
 */
public class ReceiveMessagePresenter implements ReceiveMessageOutputBoundary {
    private final DefaultListModel<String> messageModel;
    private final AttachmentRegistry attachmentRegistry;

    /** Basic constructor.
     *
     * @param messageModel message list to update when message is received
     */
    public ReceiveMessagePresenter(DefaultListModel<String> messageModel, AttachmentRegistry attachmentRegistry) {
        this.messageModel = messageModel;
        this.attachmentRegistry = attachmentRegistry;
    }

    @Override
    public void displayMessage(ReceiveMessageOutputData outputData) {
        Attachment attachment = outputData.getAttachment();

        String formattedMessage;
        if (attachment != null) {
            formattedMessage = String.format(
                    "[%s] %s: %s | %s",
                    outputData.getTimestamp(),
                    outputData.getSender(),
                    outputData.getContent(),
                    attachment.getName()
            );
        } else {
            formattedMessage = String.format(
                    "[%s] %s: %s",
                    outputData.getTimestamp(),
                    outputData.getSender(),
                    outputData.getContent()
            );
        }

        String finalFormattedMessage = formattedMessage;

        SwingUtilities.invokeLater(() -> {
            messageModel.addElement(finalFormattedMessage);

            if (attachment != null) {
                int index = messageModel.getSize() - 1;
                attachmentRegistry.registerAttachment(index, attachment);
            }
        });

    }

}
