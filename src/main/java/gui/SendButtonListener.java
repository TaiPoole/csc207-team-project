package gui;

import client.sendmessage.SendMessageController;
import server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

/** SendButtonListener class.
 *  A type of ActionListener (from java.awt)
 *  Holds relevant logic such as message lists, send message controllers, and file picking.
 */
public class SendButtonListener implements ActionListener {
    private final JTextField messageField;
    private final PickFileListener filePicker;
    private final SendMessageController sendMessageController;
    private final JTextField channelIdField;

    /** Basic Constructor.
     *
     * @param messageField message list
     * @param filePicker file picker listener (related to the ui parts of the file prompter)
     * @param sendMessageController to connect it with its interactor
     */
    public SendButtonListener(JTextField messageField,
                              PickFileListener filePicker,
                              SendMessageController sendMessageController, JTextField channelIdField) {
        this.messageField = messageField;
        this.filePicker = filePicker;
        this.sendMessageController = sendMessageController;
        this.channelIdField = channelIdField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = messageField.getText().trim();
        String channelId = channelIdField.getText();
        if (filePicker.hasAttachment()) {
            sendMessageController.sendMessageWithAttachment(message, filePicker.getAttachment(),
                    Server.getChannel(channelId));
            filePicker.clearAttachment();
        } else if (!message.isEmpty()) {
            sendMessageController.sendMessage(message, Server.getChannel(channelId));
        }
        messageField.setText("");
    }

}