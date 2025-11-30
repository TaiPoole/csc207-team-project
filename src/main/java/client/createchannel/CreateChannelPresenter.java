package client.createchannel;

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

    public CreateChannelPresenter(DefaultListModel<String> channelModel,
                                  JLabel statusLabel,
                                  JDialog dialog) {
        this.channelModel = channelModel;
        this.statusLabel = statusLabel;
        this.dialog = dialog;
    }

    @Override
    public void prepareSuccessView(CreateChannelOutputData data) {
        SwingUtilities.invokeLater(() -> {
            String displayName = "# " + data.getChannelName();

            if (!channelExists(displayName)) {
                channelModel.addElement(displayName);
            }

            statusLabel.setText(
                    "Channel \"" + data.getChannelName() + "\" created."
            );

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
