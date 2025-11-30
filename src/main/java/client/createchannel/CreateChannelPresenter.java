package client.createchannel;

import client.Client;
import interfaceadapter.ChatViewModel;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

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

    /**
     * Constructs the presenter.
     *
     * @param channelModel the list model of channels
     * @param statusLabel  the label to display status messages
     * @param dialog       the dialog that owns this presenter
     * @param chatViewModel the chat view model
     * @param client       the client instance
     */
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

    /**
     * Updates the UI for a successful channel creation.
     *
     * @param data the output data
     */
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

    /**
     * Displays a failure message.
     *
     * @param errorMessage the error message
     */
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
