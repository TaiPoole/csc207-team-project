package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class GUI {

    private static Border BorderBox() {
        return new CompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 90), 1),
                new EmptyBorder(4, 4, 4, 4)
        );
    }

    // Color Module
    private static void applyYukiTheme(Component c) {
        Color mainBg = new Color(32, 34, 37);
        Color panelBg = new Color(47, 49, 54);
        Color text = new Color(232, 232, 232);
        Color buttonBg = new Color(255, 255, 255); //it doesn't work, idk
        Color buttonText = new Color(0, 0, 0);

        applyColors(c, mainBg, panelBg, text, buttonBg, buttonText);
    }

    private static void applyColors(Component c, Color mainBg, Color panelBg, Color text, Color buttonBg,
                                    Color buttonText) {
        if (c instanceof JButton) {
            c.setBackground(buttonBg);
            c.setForeground(buttonText);
        } else if (c instanceof JPanel) {
            c.setBackground(panelBg);
            c.setForeground(text);
        } else if (c instanceof JScrollPane) {
            c.setBackground(panelBg);
            c.setForeground(text);
            ((JScrollPane) c).getViewport().setBackground(panelBg);
        } else {
            c.setBackground(mainBg);
            c.setForeground(text);
        }
        if (c instanceof JComponent) {
            for (Component child : ((JComponent) c).getComponents()) {
                applyColors(child, mainBg, panelBg, text, buttonBg, buttonText);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Right column : settingsPanel, channelSearchPanel, channelListScroll, channelManagePanel
            JPanel rightBox = new JPanel();
            rightBox.setLayout(new BoxLayout(rightBox, BoxLayout.Y_AXIS));
            rightBox.setPreferredSize(new Dimension(400, 800));

            // settingsPanel
            JPanel settingsPanel = new JPanel();
            settingsPanel.setPreferredSize(new Dimension(800,80));
            settingsPanel.setLayout(new BorderLayout(5, 5));
            settingsPanel.setBorder(BorderBox());
            settingsPanel.add(new JTextField("User Name"),  BorderLayout.WEST);
            settingsPanel.add(new JButton("New"),  BorderLayout.CENTER);
            settingsPanel.add(new JButton("Theme: Dark"), BorderLayout.EAST);
            rightBox.add(settingsPanel);
            rightBox.add(Box.createVerticalStrut(8));

            // channelSearchPanel
            JPanel channelSearchPanel = new JPanel();
            channelSearchPanel.setPreferredSize(new Dimension(800,80));
            channelSearchPanel.setLayout(new BorderLayout(5, 5));
            channelSearchPanel.setBorder(BorderBox());
            channelSearchPanel.add(new JTextField("Channel ID:"), BorderLayout.CENTER);
            channelSearchPanel.add(new JButton("Join") ,BorderLayout.EAST);
            rightBox.add(channelSearchPanel);
            rightBox.add(Box.createVerticalStrut(8));

            // channelListScroll
            DefaultListModel<String> channelModel = new DefaultListModel<>();
            channelModel.addElement("# this is a channel");
            channelModel.addElement("# this is a channel as well");
            channelModel.addElement("# ok another channel");
            JList<String> channelList = new JList<>(channelModel);
            JScrollPane channelListScroll = new JScrollPane(channelList);
            channelListScroll.setPreferredSize(new Dimension(800,540));
            channelListScroll.setBorder(BorderBox());
            rightBox.add(channelListScroll);
            rightBox.add(Box.createVerticalStrut(8));

            // channelManagePanel
            JPanel channelManagePanel = new JPanel();
            channelManagePanel.setPreferredSize(new Dimension(800,80));
            channelManagePanel.setLayout(new BorderLayout(5, 5));
            channelManagePanel.setBorder(BorderBox());
            // "+" button that opens the add-channel pop-up
            JButton addChannelButton = new JButton("+");
            addChannelButton.addActionListener(e -> gui_addchannel.showDialog(channelModel));
            channelManagePanel.add(addChannelButton, BorderLayout.WEST);

            //channelManagePanel.add(new JButton("+"), BorderLayout.WEST);
            channelManagePanel.add(new JTextField("Name:"), BorderLayout.CENTER);
            channelManagePanel.add(new JButton("Manage"), BorderLayout.EAST);
            rightBox.add(channelManagePanel);
            rightBox.add(Box.createVerticalStrut(8));

            // Left column : searchPanel, messageScroll, sendPanel
            JPanel leftBox = new JPanel();
            leftBox.setLayout(new BoxLayout(leftBox, BoxLayout.Y_AXIS));
            leftBox.setPreferredSize(new Dimension(800, 800));

            // searchPanel
            JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
            searchPanel.setPreferredSize(new Dimension(800,80));
            searchPanel.setBorder(BorderBox());
            searchPanel.add(new JTextField("Search:"),  BorderLayout.CENTER);
            searchPanel.add(new JButton("Go"),   BorderLayout.EAST);
            leftBox.add(searchPanel);
            leftBox.add(Box.createVerticalStrut(8));

            // messageScroll // IDK how the messages would be displayed so I just make it a Scroll-like right now.
            DefaultListModel<String> messageModel = new DefaultListModel<>();
            messageModel.addElement("THIS IS WHERE THE MESSAGES GO");
            JList<String> messageList = new JList<>(messageModel);
            JScrollPane messageScroll = new JScrollPane(messageList);
            messageScroll.setPreferredSize(new Dimension(800,520));
            messageScroll.setBorder(BorderBox());
            leftBox.add(messageScroll);
            leftBox.add(Box.createVerticalStrut(8));

            // sendPanel
            JPanel sendPanel = new JPanel(new BorderLayout(5, 5));
            sendPanel.setPreferredSize(new Dimension(800,200));
            sendPanel.setBorder(BorderBox());
            sendPanel.add(new JButton("Add File"),   BorderLayout.WEST);
            sendPanel.add(new JTextField("Type your message here..."),    BorderLayout.CENTER);
            sendPanel.add(new JButton("Send"), BorderLayout.EAST);
            leftBox.add(sendPanel);

            //Main Panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            mainPanel.add(leftBox);
            mainPanel.add(Box.createHorizontalStrut(8));
            mainPanel.add(rightBox);
            applyYukiTheme(mainPanel);

            // Main Frame
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
