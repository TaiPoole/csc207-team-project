package gui;

import client.sendmessage.SendMessageController;
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

    /** Basic Constructor.
     *
     * @param messageField message list
     * @param filePicker file picker listener (related to the ui parts of the file prompter)
     * @param sendMessageController to connect it with its interactor
     */
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