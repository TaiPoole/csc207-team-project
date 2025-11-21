package gui;

import javax.swing.*;
import java.awt.*;

public class PermissionsDialog extends JDialog {
    private JComboBox<String> comboBox;
    private JTextField textField;
    private JButton okButton;
    private JButton cancelButton;

    public PermissionsDialog(Frame parent) {
        super(parent, "Permissions", true);
        initComponents();
        layoutComponents();

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        String[] choices = {"Perm A", "Perm B", "Perm C"};
        comboBox = new JComboBox<>(choices);

        textField = new JTextField(20);

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboPanel.add(new JLabel("Select Permissions:"));
        comboPanel.add(comboBox);

        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textPanel.add(new JLabel("Enter name:"));
        textPanel.add(textField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(comboPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(textPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

}