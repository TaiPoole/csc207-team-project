package gui;

import client.sendmessage.SendMessageController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendButtonListener implements ActionListener {
    private final JTextField messageField;
    private final PickFileListener filePicker;
    private final SendMessageController sendMessageController;

    public SendButtonListener(JTextField messageField,
                              PickFileListener filePicker,
                              SendMessageController sendMessageController) {
        this.messageField = messageField;
        this.filePicker = filePicker;
        this.sendMessageController = sendMessageController;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String message = messageField.getText().trim();

        if (filePicker.hasAttachment()) {
            sendMessageController.sendMessageWithAttachment(message, filePicker.getAttachment());
            filePicker.clearAttachment();
        } else if (!message.isEmpty()) {
            sendMessageController.sendMessage(message);
        }
        messageField.setText("");
    }

}