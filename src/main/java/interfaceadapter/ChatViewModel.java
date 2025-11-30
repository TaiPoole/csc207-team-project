package interfaceadapter;

import common.Attachment;

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
    private final Map<String, List<Attachment>> attachmentsByChannel = new HashMap<>();
    private String activeChannel = "general";

    public ChatViewModel(DefaultListModel<String> messageModel) {
        this.messageModel = messageModel;
    }

    public DefaultListModel<String> getMessageModel() {
        return messageModel;
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
        attachmentsByChannel.computeIfAbsent(channelId, c -> new ArrayList<>());

        // Refresh the UI list
        messageModel.clear();
        for (String msg : messagesByChannel.get(channelId)) {
            messageModel.addElement(msg);
        }
    }

    public void addMessage(String channelId, String formattedMessage) {
        addMessage(channelId, formattedMessage, null);
    }

    public void addMessage(String channelId, String formattedMessage, Attachment attachment) {
        if (channelId == null || channelId.isEmpty()) {
            channelId = "general";
        }

        List<String> msgs =
                messagesByChannel.computeIfAbsent(channelId, c -> new ArrayList<>());
        List<Attachment> atts =
                attachmentsByChannel.computeIfAbsent(channelId, c -> new ArrayList<>());

        msgs.add(formattedMessage);
        atts.add(attachment); //null if no attachment


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
}
