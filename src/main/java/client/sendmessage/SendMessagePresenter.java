package client.sendmessage;

import common.Attachment;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import interfaceadapter.ChatViewModel;


/** Manager for send message outputs.
 *  follows the restrictions set by SendMessageOutputBoundary
 *  in charge of updating the UI when a message is sent
 */
public class SendMessagePresenter implements SendMessageOutputBoundary {
    private final ChatViewModel chatViewModel;


    /** Basic constructor.
     *
     * @param chatViewModel message list to update when message is sent
     */
    public SendMessagePresenter(ChatViewModel chatViewModel) {
        this.chatViewModel = chatViewModel;
    }
    @Override
    public void prepareSuccessView(SendMessageOutputData outputData) {
        SwingUtilities.invokeLater(() -> {
            String sender = outputData.getSender();
            String content = outputData.getMessageContent();
            String timestamp = outputData.getTimestamp();
            Attachment attachment = outputData.getAttachment();

            if (content == null) {
                content = "";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("[").append(timestamp).append("] ")
                    .append(sender).append(": ");

            if (!content.isEmpty()) {
                sb.append(content);
            }

            if (attachment != null) {
                if (!content.isEmpty()) {
                    sb.append(" ");
                }
                sb.append("[file: ").append(attachment.getName()).append("]");
            }

            String channelId = chatViewModel.getActiveChannel();
            if (channelId == null || channelId.isEmpty()) {
                channelId = "general";
            }

            chatViewModel.addMessage(channelId, sb.toString(), attachment);
        });
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        SwingUtilities.invokeLater(() -> {
            String channelId = chatViewModel.getActiveChannel();
            if (channelId == null || channelId.isEmpty()) {
                channelId = "general";
            }
            chatViewModel.addMessage(channelId, "ERROR: " + errorMessage);
        });
    }
}
