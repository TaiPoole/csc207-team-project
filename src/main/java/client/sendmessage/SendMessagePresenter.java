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
    //private final DefaultListModel<String> messageModel;
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

            if (outputData.getAttachment() != null) {
                Attachment attachment = outputData.getAttachment();
                String fileMessage = String.format("[%s] %s: %s | %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        outputData.getMessageContent(),
                        attachment.getName()
                );
                chatViewModel.addMessage(chatViewModel.getActiveChannel(), fileMessage);



            } else {
                String formattedMessage = String.format("[%s] %s: %s",
                        outputData.getTimestamp(),
                        outputData.getSender(),
                        outputData.getMessageContent()
                );
                chatViewModel.addMessage(chatViewModel.getActiveChannel(), formattedMessage);

            }
        });
    }

    @Override
    public void prepareFailureView(String error) {
        SwingUtilities.invokeLater(() ->
                chatViewModel.addMessage(chatViewModel.getActiveChannel(), "ERROR: " + error)
        );
    }
}
