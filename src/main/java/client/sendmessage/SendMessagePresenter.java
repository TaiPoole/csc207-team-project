package client.sendmessage;

import common.Attachment;
import interfaceadapter.AttachmentRegistry;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

/** Manager for send message outputs.
 *  follows the restrictions set by SendMessageOutputBoundary
 *  in charge of updating the UI when a message is sent
 */
public class SendMessagePresenter implements SendMessageOutputBoundary {
    private final DefaultListModel<String> messageModel;
    private final AttachmentRegistry attachmentRegistry;


    /** Basic constructor.
     *
     * @param messageModel message list to update when message is sent
     */
    public SendMessagePresenter(DefaultListModel<String> messageModel, AttachmentRegistry attachmentRegistry) {
        this.messageModel = messageModel;
        this.attachmentRegistry = attachmentRegistry;
    }

    @Override
    public void prepareSuccessView(SendMessageOutputData outputData) {
        Attachment attachment = outputData.getAttachment();

        String formattedMessage;
        if (attachment != null) {
            formattedMessage = String.format(
                    "[%s] %s: %s | %s",
                    outputData.getTimestamp(),
                    outputData.getSender(),
                    outputData.getMessageContent(),
                    attachment.getName()
            );
        } else {
            formattedMessage = String.format(
                    "[%s] %s: %s",
                    outputData.getTimestamp(),
                    outputData.getSender(),
                    outputData.getMessageContent()
            );
        }

        String finalFormattedMessage = formattedMessage;

        SwingUtilities.invokeLater(() -> {
            // Add to visible list
            messageModel.addElement(finalFormattedMessage);

            // Remember the attachment (if any) for this row
            if (attachment != null) {
                int index = messageModel.getSize() - 1;
                attachmentRegistry.registerAttachment(index, attachment);
            }
        });
    }

    @Override
    public void prepareFailureView(String error) {
        SwingUtilities.invokeLater(() -> messageModel.addElement("ERROR: " + error));
    }
}
