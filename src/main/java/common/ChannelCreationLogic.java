package common;

import gui.AddChannelDialog;

import javax.swing.*;

public class ChannelCreationLogic {
    public static boolean clientLogic(Message message, JFrame frame) {
        // channel creation SUCCESS
        if (message instanceof common.ChannelCreationSuccessMessage) {
            common.ChannelCreationSuccessMessage success =
                    (common.ChannelCreationSuccessMessage) message;

            SwingUtilities.invokeLater(() -> {
                AddChannelDialog.closeIfOpen();

                JOptionPane.showMessageDialog(
                        frame,
                        "Channel created: " + success.getChannelName(),
                        "Channel Created",
                        JOptionPane.INFORMATION_MESSAGE
                );

            });
            return true;
        }

        // channel creation FAILURE
        if (message instanceof common.ChannelCreationErrorMessage) {
            common.ChannelCreationErrorMessage error =
                    (common.ChannelCreationErrorMessage) message;

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        frame,
                        "Failed to create channel: " + error.getContent(),
                        "Channel Creation Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            });
            return true;
        }
        return false;
    }
}
