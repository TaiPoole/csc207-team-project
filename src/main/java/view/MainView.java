package view;

import client.Client;
import common.RandomNameGenerator;
import gui.PermissionsDialog;
import gui.Themes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * The main messaging UI.
 */
public class MainView extends JPanel {

    private  final Client client;
    private final RandomNameGenerator nameGenerator;

    // ListModels
    private final DefaultListModel<String> channelModel = new DefaultListModel<>();
    private final DefaultListModel<String> messageModel = new DefaultListModel<>();

    // TextFields
    private final JTextField usernameField = new JTextField("User Name");
    private final JTextField channelIdField = new JTextField("Channel ID:");
    private final JTextField channelNameField = new JTextField("Channel Name:");
    private final JTextField searchField = new JTextField("Search:");
    private final JTextField messageField = new JTextField("");

    // Buttons
    private final JButton themeButton = new JButton("Dark");

    // Theme manager from gui package
    private final Themes themes = new Themes();

    /**
     * Constructs the main messaging UI layout.
     */
    public MainView(Client client, RandomNameGenerator nameGenerator) {
        this.client = client;
        this.nameGenerator = nameGenerator;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JPanel leftBox = buildLeftPane();
        JPanel rightBox = buildRightPane();

        add(leftBox);
        add(Box.createHorizontalStrut(8));
        add(rightBox);

        // Initial data
        channelModel.addElement("# this is a channel");
        channelModel.addElement("# this is a channel as well");
        channelModel.addElement("# ok another channel");
        messageModel.addElement("THIS IS WHERE THE MESSAGES GO");

        // Initialize username from client, or generate one if empty
        String currentName = client.getUsername();
        if (currentName == null || currentName.isEmpty()) {
            currentName = nameGenerator.generate();
            client.setUsername(currentName);
        }
        usernameField.setText(currentName);

        themes.theme.applyPalette(this);
        themeButton.setText(themes.theme.getClass().getName().substring(4));
    }

    private JPanel buildLeftPane() {
        JPanel leftBox = new JPanel();
        leftBox.setLayout(new BoxLayout(leftBox, BoxLayout.Y_AXIS));
        leftBox.setPreferredSize(new Dimension(800, 800));

        // searchPanel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setPreferredSize(new Dimension(800, 80));
        searchPanel.setBorder(borderBox());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(new JButton("Go"), BorderLayout.EAST);
        leftBox.add(searchPanel);
        leftBox.add(Box.createVerticalStrut(8));

        // messageScroll
        JList<String> messageList = new JList<>(messageModel);
        JScrollPane messageScroll = new JScrollPane(messageList);
        messageScroll.setPreferredSize(new Dimension(800, 520));
        messageScroll.setBorder(borderBox());
        leftBox.add(messageScroll);
        leftBox.add(Box.createVerticalStrut(8));

        // sendPanel
        JPanel sendPanel = new JPanel(new BorderLayout(5, 5));
        sendPanel.setPreferredSize(new Dimension(800, 200));
        sendPanel.setBorder(borderBox());

        JButton addFileButton = new JButton("Add File");
        JButton sendButton = new JButton("Send");

        sendPanel.add(addFileButton, BorderLayout.WEST);
        sendPanel.add(messageField, BorderLayout.CENTER);
        sendPanel.add(sendButton, BorderLayout.EAST);

        leftBox.add(sendPanel);

        // AddFile popup
        addFileButton.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            Frame owner = null;
            if (w instanceof Frame) {
                owner = (Frame) w;
            }
            AddFileView dialog = new AddFileView(owner);
            dialog.setVisible(true);
        });

        // Sending messages (locally, no client)
        sendButton.addActionListener(e -> sendCurrentMessage());
        messageField.addActionListener(e -> sendCurrentMessage());

        return leftBox;
    }

    private JPanel buildRightPane() {
        JPanel rightBox = new JPanel();
        rightBox.setLayout(new BoxLayout(rightBox, BoxLayout.Y_AXIS));
        rightBox.setPreferredSize(new Dimension(400, 800));

        // settingsPanel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setPreferredSize(new Dimension(800, 80));
        settingsPanel.setLayout(new BorderLayout(5, 5));
        settingsPanel.setBorder(borderBox());
        settingsPanel.add(usernameField, BorderLayout.WEST);
        JButton newButton = new JButton("New");
        settingsPanel.add(newButton, BorderLayout.CENTER);
        settingsPanel.add(themeButton, BorderLayout.EAST);
        rightBox.add(settingsPanel);
        rightBox.add(Box.createVerticalStrut(8));

        // Random name generation
        newButton.addActionListener(e -> {
            String newName = nameGenerator.generate();
            client.setUsername(newName);
            usernameField.setText(newName);
        });

        // themeButton
        themeButton.addActionListener(e -> {
            themes.cyclePalette();
            themeButton.setText(themes.theme.getClass().getName().substring(4));
            themes.theme.applyPalette(this);
        });

        // channelSearchPanel
        JPanel channelSearchPanel = new JPanel();
        channelSearchPanel.setPreferredSize(new Dimension(800, 80));
        channelSearchPanel.setLayout(new BorderLayout(5, 5));
        channelSearchPanel.setBorder(borderBox());
        channelSearchPanel.add(channelIdField, BorderLayout.CENTER);
        channelSearchPanel.add(new JButton("Join"), BorderLayout.EAST);
        rightBox.add(channelSearchPanel);
        rightBox.add(Box.createVerticalStrut(8));

        // channelListScroll
        JList<String> channelList = new JList<>(channelModel);
        JScrollPane channelListScroll = new JScrollPane(channelList);
        channelListScroll.setPreferredSize(new Dimension(800, 540));
        channelListScroll.setBorder(borderBox());
        rightBox.add(channelListScroll);
        rightBox.add(Box.createVerticalStrut(8));

        // channelManagePanel
        JPanel channelManagePanel = new JPanel();
        channelManagePanel.setPreferredSize(new Dimension(800, 80));
        channelManagePanel.setLayout(new BorderLayout(5, 5));
        channelManagePanel.setBorder(borderBox());
        channelManagePanel.add(new JButton("+"), BorderLayout.WEST);
        channelManagePanel.add(channelNameField, BorderLayout.CENTER);

        JButton permsButton = new JButton("Manage");
        channelManagePanel.add(permsButton, BorderLayout.EAST);

        // Perms popup
        permsButton.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            Frame owner = null;
            if (w instanceof Frame) {
                owner = (Frame) w;
            }
            PermissionsDialog dialog = new PermissionsDialog(owner);
            dialog.setVisible(true);
        });

        rightBox.add(channelManagePanel);
        rightBox.add(Box.createVerticalStrut(8));

        return rightBox;
    }

    private void sendCurrentMessage() {
        String text = messageField.getText();
        if (!text.isEmpty()) {
            messageModel.addElement(text);
            messageField.setText("");
        }
    }

    private Border borderBox() {
        return new CompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 90), 1),
                new EmptyBorder(4, 4, 4, 4)
        );
    }
}