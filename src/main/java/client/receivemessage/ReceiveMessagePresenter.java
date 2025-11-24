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
            }
        });

    }

}
