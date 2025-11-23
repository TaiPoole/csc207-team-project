package view;

import javax.swing.*;
import java.awt.*;

/**
 * Popup dialog shown when the "Add File" button is clicked.
 */
public class AddFileView extends JDialog {

    public AddFileView(Frame owner) {
        super(owner, "Add File", true);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));
        row1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("File name:");
        JTextField nameField = new JTextField();
        nameField.setColumns(20);
        JButton addButton = new JButton("Add");

        row1.add(nameLabel);
        row1.add(Box.createHorizontalStrut(8));
        row1.add(nameField);
        row1.add(Box.createHorizontalStrut(8));
        row1.add(addButton);

        JPanel row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
        row2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton sendFileButton = new JButton("Send File");
        row2.add(sendFileButton);

        content.add(row1);
        content.add(Box.createVerticalStrut(10));
        content.add(row2);
        setContentPane(content);
        setPreferredSize(new Dimension(400, 100));
        pack();
        setLocationRelativeTo(owner);
    }
}