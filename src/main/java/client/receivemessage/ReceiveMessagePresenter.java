package client.receivemessage;

import common.Attachment;
import javax.swing.*;

public class ReceiveMessagePresenter implements ReceiveMessageOutputBoundary {
    private final DefaultListModel<String> messageModel;

    public ReceiveMessagePresenter(DefaultListModel<String> messageModel) {
        this.messageModel = messageModel;
    }

    @Override
    public void displayMessage(ReceiveMessageOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            String formattedMessage = String.format("[%s] %s: %s",
                    outputData.getTimestamp(),
                    outputData.getSender(),
                    outputData.getContent()
            );

            messageModel.addElement(formattedMessage);

            if (outputData.getAttachment() != null) {
                Attachment attachment = outputData.getAttachment();
                // TODO: handle attachments
            }
        });

    }

}
