package gui;

import client.Client;

import javax.swing.*;
import java.awt.*;

public class AddChannelButton extends Button {

    private final JFrame parent;
    private final Client client;

    public AddChannelButton(JFrame parent, Client client) {
        super("+");
        this.parent = parent;
        this.client = client;

        addActionListener(e -> {
            JFrame owner = this.parent;

            if (owner == null) {
                Window w = SwingUtilities.getWindowAncestor(this);
                if (w instanceof JFrame) {
                    owner = (JFrame) w;
                }
            }

            if (owner != null) {
                AddChannelDialog dialog = new AddChannelDialog(owner, client);
                dialog.setVisible(true);
            } else {
                AddChannelDialog dialog = new AddChannelDialog(null, client);
                dialog.setVisible(true);
            }
        });
    }
}
