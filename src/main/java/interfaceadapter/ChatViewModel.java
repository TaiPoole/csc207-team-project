package interfaceadapter;

import client.Client;
import common.Attachment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;

/**
 * Holds chat messages per channel and keeps the Swing list in sync
 * with the currently active channel.
 */
public class ChatViewModel {

    private final DefaultListModel<String> messageModel;
    private final Map<String, List<String>> messagesByChannel = new HashMap<>();
    private final Map<String, List<Attachment>> attachmentsByChannel = new HashMap<>();
    private String activeChannel = "general";

    /**
     * Constructs the chat view model.
     *
     * @param messageModel the Swing list model backing the UI
     */
    public ChatViewModel(DefaultListModel<String> messageModel) {
        this.messageModel = messageModel;
    }

    public DefaultListModel<String> getMessageModel() {
        return messageModel;
    }

    /**
     * Returns the active channel name.
     *
     * @return the active channel
     */
    public String getActiveChannel() {
        return activeChannel;
    }

    /**
     * Sets the active channel and refreshes the message list.
     *
     * @param channelId the channel to show
     */
    public void setActiveChannel(String channelId) {
        if (channelId == null || channelId.isEmpty()) {
            channelId = "general";
        }

        activeChannel = channelId;
        messagesByChannel.computeIfAbsent(channelId, c -> new ArrayList<>());
        attachmentsByChannel.computeIfAbsent(channelId, c -> new ArrayList<>());

        // Refresh the UI list
        messageModel.clear();
        for (String msg : messagesByChannel.get(channelId)) {
            messageModel.addElement(msg);
        }
    }

    /**
     * Adds a message to the specified channel.
     *
     * @param channelId the channel
     * @param formattedMessage the message text
     */
    public void addMessage(String channelId, String formattedMessage) {
        addMessage(channelId, formattedMessage, null);
    }

    /**
     * Adds a message and attachment to the channel.
     *
     * @param channelId channel to add into
     * @param formattedMessage message text
     * @param attachment optional attachment
     */
    public void addMessage(String channelId, String formattedMessage, Attachment attachment) {
        if (channelId == null || channelId.isEmpty()) {
            channelId = "general";
        }

        List<String> msgs =
                messagesByChannel.computeIfAbsent(channelId, c -> new ArrayList<>());
        List<Attachment> atts =
                attachmentsByChannel.computeIfAbsent(channelId, c -> new ArrayList<>());

        msgs.add(formattedMessage);
        atts.add(attachment); // null if no attachment


        if (channelId.equals(activeChannel)) {
            messageModel.addElement(formattedMessage);
        }
    }

    /**
     * Returns the attachment (if any) corresponding to the message at the given
     * index in the currently active channel. Returns null if there is no
     * attachment or the index is out of range.
     */
    public Attachment getAttachmentForIndex(int index) {
        if (activeChannel == null || activeChannel.isEmpty()) {
            activeChannel = "general";
        }

        List<Attachment> atts = attachmentsByChannel.get(activeChannel);
        if (atts == null || index < 0 || index >= atts.size()) {
            return null;
        }
        return atts.get(index);
    }

    /**
     * Returns a copy of the messages in the currently active channel.
     * Used by the search use case so it only searches the current channel.
     */
    public List<String> getMessagesForActiveChannel() {
        List<String> msgs = messagesByChannel.get(activeChannel);
        if (msgs == null) {
            return java.util.Collections.emptyList();
        }
        return new ArrayList<>(msgs);
    }

    /** Joins a channel and updates the chatViewModel.
     *
     * @param rawText input field
     * @param client client
     * @param channelModel the channel model
     */
    public void joinChannel(String rawText, Client client, DefaultListModel<String> channelModel) {
        String channelId = rawText.replace("Channel ID: ", "").trim();

        if (channelModel.contains("# " + channelId)) {
            this.setActiveChannel(channelId);
            client.setCurrentChannel(channelId);
        }
    }
}
