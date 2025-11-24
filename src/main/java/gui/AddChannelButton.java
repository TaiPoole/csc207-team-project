package gui;

import client.Client;

import javax.swing.*;

public class AddChannelButton extends Button {

    public AddChannelButton(JFrame parent, Client client) {
        super("+");

        addActionListener(e -> {
            AddChannelDialog dialog = new AddChannelDialog(parent, client);
            dialog.setVisible(true);
        });
    }
}
