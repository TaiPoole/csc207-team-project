package gui;

import client.Client;
import common.Attachment;
import common.CreateChannelMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AddChannelDialog extends JDialog {
    public static AddChannelDialog currentInstance = null;
    private final JTextField channelNameField = new JTextField(20);

    public AddChannelDialog(JFrame parent, Client client) {
        super(parent, "Create New Channel", true);
        currentInstance = this;

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(new JLabel("Channel name: "), BorderLayout.WEST);
        inputPanel.add(channelNameField, BorderLayout.CENTER);

        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        pack();
        setLocationRelativeTo(parent);

        cancelButton.addActionListener(e -> dispose());

        addButton.addActionListener(e -> {
            String name = channelNameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Channel name cannot be empty.",
                        "Invalid name",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                client.sendMessage(new common.CreateChannelMessage(
                        client.getUsername(),
                        name
                ) {
                    @Override
                    public Attachment getAttachment() {
                        return null;
                    }
                });
                //dispose();
                newBehave(client, name);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to create channel: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void newBehave(Client client, String name) {
        try {
            client.sendMessage(new CreateChannelMessage(client.getUsername(), name) {
                @Override
                public Attachment getAttachment() {
                    return null;
                }
            });
            // DO NOT close here
            // Server will respond success or failure
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to contact server: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void closeIfOpen() {
        if (currentInstance != null) {
            currentInstance.dispose();
            currentInstance = null;
        }
    }
}
