package view;

import client.Client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Dialog for creating a new channel.
 * Lets the user enter a channel name, click "Add" to create it,
 * shows a short success/failure message, and has a Close button.
 */
public class AddChannelDialog extends JDialog {

    private final JTextField channelNameField;
    private final JLabel statusLabel;
    private final JButton addButton;
    private final JButton closeButton;

    /**
     * @param owner         parent frame (Main window)
     * @param channelModel  the model that backs the channel JList in MainView
     * @param client        the connected client (to notify the server)
     */
    public AddChannelDialog(Frame owner,
                            DefaultListModel<String> channelModel,
                            Client client) {
        super(owner, "Add Channel", true);

        this.channelNameField = new JTextField(20);
        this.statusLabel = new JLabel(" ");
        this.addButton = new JButton("Add");
        this.closeButton = new JButton("Close");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel namePanel = new JPanel(new BorderLayout(5, 5));
        namePanel.add(new JLabel("Channel name:"), BorderLayout.WEST);
        namePanel.add(channelNameField, BorderLayout.CENTER);

        statusLabel.setPreferredSize(new Dimension(260, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(closeButton);

        mainPanel.add(namePanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(statusLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);


        // When "Add" is clicked, try to create the channel.
        addButton.addActionListener(e -> {
            String name = channelNameField.getText().trim();

            if (name.isEmpty()) {
                statusLabel.setText("Please enter a channel name.");
                return;
            }

            if (channelExists(channelModel, name)) {
                statusLabel.setText("Channel \"" + name + "\" already exists.");
                return;
            }

            try {
                String command = "/create-channel " + name;
                client.sendMessage(command);

                channelModel.addElement("# " + name);

                statusLabel.setText("Channel \"" + name + "\" created.");
                channelNameField.setText("");
            } catch (IOException ex) {
                statusLabel.setText("Failed to contact server.");
            }
        });

        // Close button just closes the dialog.
        closeButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Checks if the channel already exists in the list model.
     */
    private boolean channelExists(DefaultListModel<String> model, String name) {
        String formatted = "# " + name;
        for (int i = 0; i < model.size(); i++) {
            if (formatted.equals(model.get(i))) {
                return true;
            }
        }
        return false;
    }
}
