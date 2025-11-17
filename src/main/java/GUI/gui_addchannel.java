package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class gui_addchannel {

    public static void showDialog(DefaultListModel<String> channelModel) {
        JDialog dialog = new JDialog((Frame) null, "Add Channel", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        JLabel nameLabel = new JLabel("Channel name:");
        JTextField nameField = new JTextField();
        centerPanel.add(nameLabel, BorderLayout.WEST);
        centerPanel.add(nameField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                if (!name.startsWith("#")) {
                    name = "# " + name;
                }
                channelModel.addElement(name);
            }
            dialog.dispose();
        });

        buttonPanel.add(addButton);

        content.add(centerPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(content);

        dialog.setPreferredSize(new Dimension(400, 200));
        dialog.setMinimumSize(new Dimension(400, 200));

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

}
