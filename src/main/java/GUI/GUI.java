package GUI;

import javax.swing.*;
import java.awt.event.*;

public class GUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JPanel rightBox = new JPanel();
            rightBox.setLayout(new BoxLayout(rightBox, BoxLayout.Y_AXIS));
            rightBox.setSize(400, 800);
            JPanel settingsPanel = new JPanel();
            settingsPanel.add(new JTextField("User Name"));
            settingsPanel.add(new JButton("New"));
            settingsPanel.add(new JButton("Theme: Dark"));
            rightBox.add(settingsPanel);
            JPanel channelSearchPanel = new JPanel();
            channelSearchPanel.add(new JTextField("Channel ID:"));
            channelSearchPanel.add(new JButton("Join"));
            rightBox.add(channelSearchPanel);
            JList<String> channelList = new JList<>();
            channelList.add(new JTextField("THIS IS WHERE THE CHANNELS GO"));
            rightBox.add(channelList);
            JPanel channelManagePanel = new JPanel();
            channelManagePanel.add(new JButton("+"));
            channelManagePanel.add(new JTextField("Name:"));
            channelManagePanel.add(new JButton("Manage"));
            rightBox.add(channelManagePanel);

            JPanel leftBox = new JPanel();
            leftBox.setLayout(new BoxLayout(leftBox, BoxLayout.Y_AXIS));
            leftBox.setSize(800, 800);
            JPanel searchPanel = new JPanel();
            searchPanel.add(new JTextField("Search:"));
            searchPanel.add(new JButton("Go"));
            leftBox.add(searchPanel);
            JList<String> messagePanel = new JList<>();
            messagePanel.add(new JTextField("THIS IS WHERE THE MESSAGES GO"));
            leftBox.add(messagePanel);
            JPanel sendPanel = new JPanel();
            sendPanel.add(new JButton("Add File"));
            sendPanel.add(new JTextField("Type your message here..."));
            sendPanel.add(new JButton("Send"));
            leftBox.add(sendPanel);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            mainPanel.add(leftBox);
            mainPanel.add(rightBox);

            //TODO please don't let this be the final name
            JFrame frame = new JFrame("The really cool messaging service");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(1200, 800);
            frame.setVisible(true);
        });
    }
}
