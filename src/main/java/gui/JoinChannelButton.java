package gui;

import client.sendmessage.SendMessageInputData;
import common.Attachment;
import common.AttachmentMessage;
import common.Channel;
import common.Message;
import javax.swing.DefaultListModel;
import server.Server;

/** Subclass of Button to handle joining a channel.
 *
 */
public class JoinChannelButton extends Button {

    /** Constructor for JoinChannelButton.
     *
     * @param text text on the button
     */
    public JoinChannelButton(String text) {
        super(text);
    }

    /** Updates message model for new channel.
     *
     * @param channelId id of the channel
     */
    public void joinChannel(String channelId, DefaultListModel<String> messageModel) {
        messageModel.clear();
        messageModel.addElement("=== Joined channel: " + channelId + " ===");
        Channel c = Server.getChannel(channelId);
        for (Message message : c.getMessages()) {
            loadMessages(message, messageModel);
        }
    }

    /** load the channel's messages from before joining.
     *
     * @param message Message to be loaded
     * @param messageModel message stack for mainview
     */
    public void loadMessages(Message message, DefaultListModel<String> messageModel) {
        if (message.getAttachment() != null) {
            Attachment attachment = message.getAttachment();
            String fileMessage = String.format("[%s] %s: %s | %s",
                    message.getTimestamp(),
                    message.getUsername(),
                    message.getContent(),
                    attachment.getName()
            );
            messageModel.addElement(fileMessage);


        } else {
            String formattedMessage = String.format("[%s] %s: %s",
                    message.getTimestamp(),
                    message.getUsername(),
                    message.getContent()
            );

            messageModel.addElement(formattedMessage);
        }
    }
}
