package interfaceadapter;

import javax.swing.DefaultListModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds chat messages per channel and keeps the Swing list in sync
 * with the currently active channel.
 */
public class ChatViewModel {

    private final DefaultListModel<String> messageModel;
    private final Map<String, List<String>> messagesByChannel = new HashMap<>();
    private String activeChannel = "general";

    public ChatViewModel(DefaultListModel<String> messageModel) {
        this.messageModel = messageModel;
        messagesByChannel.put("general", new ArrayList<>());
    }

    public String getActiveChannel() {
        return activeChannel;
    }

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
}
