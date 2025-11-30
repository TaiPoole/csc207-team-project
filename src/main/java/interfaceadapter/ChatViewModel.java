package interfaceadapter;

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
    private String activeChannel = "general";

    /**
     * Constructs the chat view model.
     *
     * @param messageModel the Swing list model backing the UI
     */
    public ChatViewModel(DefaultListModel<String> messageModel) {
        this.messageModel = messageModel;
        messagesByChannel.put("general", new ArrayList<>());
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
        if (channelId == null || channelId.isEmpty()) {
            channelId = "general";
        }

        messagesByChannel
                .computeIfAbsent(channelId, c -> new ArrayList<>())
                .add(formattedMessage);

        if (channelId.equals(activeChannel)) {
            messageModel.addElement(formattedMessage);
        }
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
}
