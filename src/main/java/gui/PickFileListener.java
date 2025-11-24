package gui;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PickFileListener implements ActionListener {
    private final JFrame parentFrame;
    private final JPanel fileDisplayPanel;
    private final Client client;
    private File selectedFile;
    private byte[] fileBytes;

    public PickFileListener(JFrame parentFrame, JPanel fileDisplayPanel, Client client) {
        this.parentFrame = parentFrame;
        this.fileDisplayPanel = fileDisplayPanel;
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();

        int result = fileChooser.showOpenDialog(parentFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            try {
                fileBytes = Files.readAllBytes(selectedFile.toPath());
                String fileName = selectedFile.getName();
                displayFile(fileName);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Error reading file: " + ex.getMessage(),
                        "File Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayFile(String fileName) {
        fileDisplayPanel.removeAll();

        JPanel fileInfoPanel = new JPanel(new BorderLayout(5, 5));
        fileInfoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JLabel fileLabel = new JLabel(fileName);
        fileLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Remove button
        JButton removeButton = new JButton("x");
        removeButton.setPreferredSize(new Dimension(60, 15));
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> removeFile());

        fileInfoPanel.add(fileLabel, BorderLayout.CENTER);
        fileInfoPanel.add(removeButton, BorderLayout.EAST);

        fileDisplayPanel.add(fileInfoPanel);
        fileDisplayPanel.revalidate();
        fileDisplayPanel.repaint();
    }

    private void removeFile() {
        selectedFile = null;
        fileBytes = null;
        fileDisplayPanel.removeAll();
        fileDisplayPanel.revalidate();
        fileDisplayPanel.repaint();
    }
}