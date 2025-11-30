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
            String cleanContent = stripChannelPrefix(rawContent);
            Attachment attachment = outputData.getAttachment();

            String formattedMessage;
            if (attachment != null) {
                formattedMessage = String.format("[%s] %s: %s | %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        cleanContent,
                        attachment.getName()
                );
            } else {
                formattedMessage = String.format("[%s] %s: %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        cleanContent
                );
            }

            chatViewModel.addMessage(channelId, formattedMessage);
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
        return chatViewModel.getActiveChannel();
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
