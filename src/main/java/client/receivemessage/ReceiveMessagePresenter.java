package client.receivemessage;

import common.Attachment;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import interfaceadapter.ChatViewModel;


/** Manager for incoming received messages.
 *  follows restrictions set by ReceiveMessageOutputBoundary
 *  in charge of updating the UI when a message is received
 */
public class ReceiveMessagePresenter implements ReceiveMessageOutputBoundary {
    private final ChatViewModel chatViewModel;

    /** Basic constructor.
     *
     * @param chatViewModel message list to update when message is received
     */
    public ReceiveMessagePresenter(ChatViewModel chatViewModel) {
        this.chatViewModel = chatViewModel;
    }

    @Override
    public void displayMessage(ReceiveMessageOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            String rawContent = outputData.getContent();
            String channelId = extractChannelId(rawContent);

            if (channelId == null || channelId.isEmpty()) {
                channelId = chatViewModel.getActiveChannel();
                if (channelId == null || channelId.isEmpty()) {
                    channelId = "general";
                }
            }

            String cleanContent = stripChannelPrefix(rawContent);
            Attachment attachment = outputData.getAttachment();

            StringBuilder sb = new StringBuilder();
            sb.append("[").append(outputData.getTimestamp()).append("] ")
                    .append(outputData.getSender()).append(": ");

            if (cleanContent != null && !cleanContent.isEmpty()) {
                sb.append(cleanContent);
            }

            if (attachment != null) {
                if (cleanContent != null && !cleanContent.isEmpty()) {
                    sb.append(" ");
                }
                sb.append("[file: ").append(attachment.getName()).append("]");
            }

            chatViewModel.addMessage(channelId, sb.toString(), attachment);
        });
    }

    // Helper methods inside ReceiveMessagePresenter:

    /**
     * Extracts channel id from a message like "[general] Hello".
     * If none, falls back to the current active channel.
     */
    private String extractChannelId(String rawContent) {
        if (rawContent != null && rawContent.startsWith("[") && rawContent.contains("]")) {
            int end = rawContent.indexOf(']');
            if (end > 1) {
                return rawContent.substring(1, end); // between [ and ]
            }
        }
        // No explicit tag -> assume current active channel
        return null;
    }

    /**
     * Strips the "[channel] " prefix from the message content, if present.
     */
    private String stripChannelPrefix(String rawContent) {
        if (rawContent != null && rawContent.startsWith("[") && rawContent.contains("] ")) {
            int idx = rawContent.indexOf("] ");
            if (idx >= 0 && idx + 2 <= rawContent.length()) {
                return rawContent.substring(idx + 2);
            }
        }
        return rawContent;
    }


}
