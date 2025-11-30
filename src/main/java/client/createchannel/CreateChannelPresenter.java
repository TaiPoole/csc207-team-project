package client.createchannel;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import client.Client;
import interfaceadapter.ChatViewModel;

/**
 * Presenter for the Create Channel use case.
 * Updates the channel list model and status label in the UI.
 */
public class CreateChannelPresenter implements CreateChannelOutputBoundary {

    private final DefaultListModel<String> channelModel;
    private final JLabel statusLabel;
    private final JDialog dialog;
    private final ChatViewModel chatViewModel;
    private final Client client;

    public CreateChannelPresenter(DefaultListModel<String> channelModel,
                                  JLabel statusLabel,
                                  JDialog dialog,
                                  ChatViewModel chatViewModel,
                                  Client client) {
        this.channelModel = channelModel;
        this.statusLabel = statusLabel;
        this.dialog = dialog;
        this.chatViewModel = chatViewModel;
        this.client = client;
    }

    @Override
    public void prepareSuccessView(CreateChannelOutputData data) {
        SwingUtilities.invokeLater(() -> {
            String channelName = data.getChannelName();
            String displayName = "# " + channelName;

            if (!channelExists(displayName)) {
                channelModel.addElement(displayName);
            }

            statusLabel.setText(
                    "Channel \"" + data.getChannelName() + "\" created."
            );
            // Switch user into the new channel
            chatViewModel.setActiveChannel(channelName);
            client.setCurrentChannel(channelName);

            // Close the dialog on successful creation
            dialog.dispose();
        });
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        SwingUtilities.invokeLater(() ->
                statusLabel.setText(errorMessage)
        );
    }

    private boolean channelExists(String displayName) {
        for (int i = 0; i < channelModel.size(); i++) {
            if (displayName.equals(channelModel.get(i))) {
                return true;
            }
        }
        return false;
    }
}
