package gui;

import javax.swing.DefaultListModel;

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
        // load messages from channel and put into messageModel

    }
}
