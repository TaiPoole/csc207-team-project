package view;

import client.Client;
import client.sendmessage.SendMessageController;
import client.sendmessage.SendMessageInputBoundary;
import common.RandomNameGenerator;
import gui.PickFileListener;
import gui.SendButtonListener;
import gui.ThemeButton;
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
import javax.swing.JFrame;
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

    private final Client client;
    private final RandomNameGenerator nameGenerator;
    private final SendMessageInputBoundary sendMessageInteractor;

    // ListModels
    private final DefaultListModel<String> channelModel = new DefaultListModel<>();
    private final DefaultListModel<String> messageModel;

    // TextFields
    private final JTextField usernameField;
    private final JTextField channelIdField = new JTextField("Channel ID:");
    private final JTextField channelNameField = new JTextField("Name:");
    private final JTextField searchField = new JTextField("Search:");
    private final JTextField messageField = new JTextField("");

    // Buttons
    private final ThemeButton themeButton = new ThemeButton("Dark Mode");

    // File picker for attachments
    private PickFileListener filePicker;
    private JPanel fileDisplayPanel;

    /**
     * Constructs the main messaging UI layout.
     */
    public MainView(
            Client client,
            RandomNameGenerator nameGenerator,
            DefaultListModel<String> messageModel,
            SendMessageInputBoundary sendMessageInteractor
    ) {
        this.client = client;
        this.nameGenerator = nameGenerator;
        this.messageModel = messageModel;
        this.sendMessageInteractor = sendMessageInteractor;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        this.usernameField = new JTextField(client.getUsername());

        JPanel leftBox = buildLeftPane();
        JPanel rightBox = buildRightPane();

        add(leftBox);
        add(Box.createHorizontalStrut(8));
        add(rightBox);

        // Initial test data
        channelModel.addElement("# this is a channel");
        channelModel.addElement("# this is a channel as well");
        channelModel.addElement("# ok another channel");
        messageModel.addElement("THIS IS WHERE THE MESSAGES GO");
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

        // sendPanel with file support
        JPanel sendPanel = buildSendPanel();
        leftBox.add(sendPanel);

        return leftBox;
    }

    private JPanel buildSendPanel() {
        JPanel sendPanel = new JPanel(new BorderLayout(5, 5));
        sendPanel.setPreferredSize(new Dimension(800, 80));
        sendPanel.setBorder(borderBox());

        // Message box with text field and file display area
        JPanel messageBox = new JPanel();
        messageBox.setLayout(new BoxLayout(messageBox, BoxLayout.Y_AXIS));
        messageBox.add(messageField);

        // File display panel for showing selected files
        fileDisplayPanel = new JPanel();
        fileDisplayPanel.setLayout(new BoxLayout(fileDisplayPanel, BoxLayout.Y_AXIS));
        messageBox.add(fileDisplayPanel);

        JButton addFileButton = new JButton("Add File");
        final JButton sendButton = new JButton("Send");

        // Initialize file picker - need to get parent frame
        // Use a deferred initialization to ensure frame is available
        filePicker = new PickFileListener(null, fileDisplayPanel);

        addFileButton.addActionListener(e -> {
            // Get the frame dynamically when button is clicked
            Window window = SwingUtilities.getWindowAncestor(this);
            JFrame owner = (window instanceof JFrame) ? (JFrame) window : null;
            filePicker.setParentFrame(owner);
            filePicker.actionPerformed(e);
        });

        sendPanel.add(addFileButton, BorderLayout.WEST);
        sendPanel.add(messageBox, BorderLayout.CENTER);
        sendPanel.add(sendButton, BorderLayout.EAST);

        // Set up send message controller and listener
        SendMessageController sendMessageController = new SendMessageController(sendMessageInteractor);
        SendButtonListener sendButtonListener = new SendButtonListener(
                messageField,
                filePicker,
                sendMessageController
        );

        sendButton.addActionListener(sendButtonListener);
        messageField.addActionListener(sendButtonListener); // Allow Enter key to send

        return sendPanel;
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

        usernameField.setPreferredSize(new Dimension(125, 80));
        settingsPanel.add(usernameField, BorderLayout.WEST);

        JButton newButton = new JButton("New Name");
        newButton.setPreferredSize(new Dimension(50, 80));
        settingsPanel.add(newButton, BorderLayout.CENTER);
        settingsPanel.add(themeButton, BorderLayout.EAST);

        rightBox.add(settingsPanel);
        rightBox.add(Box.createVerticalStrut(8));

        // Random name generation button
        newButton.addActionListener(e -> {
            String newName = nameGenerator.generate();
            client.setUsername(newName);       // update local identity
            usernameField.setText(newName);    // reflect in UI
        });

        // channelSearchPanel
        JPanel channelSearchPanel = new JPanel();
        channelSearchPanel.setPreferredSize(new Dimension(800, 80));
        channelSearchPanel.setLayout(new BorderLayout(5, 5));
        channelSearchPanel.setBorder(borderBox());
        channelSearchPanel.add(channelIdField, BorderLayout.CENTER);
        JButton joinButton = new JButton("Join");
        channelSearchPanel.add(joinButton, BorderLayout.EAST);

        // Join button action - join channel by ID
        joinButton.addActionListener(e -> {
            String channelId = channelIdField.getText();
            if (channelModel.contains(channelId)) {
                joinChannel(channelId);
            }
        });




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
        JButton addChannelButton = new JButton("+");
        channelManagePanel.add(addChannelButton, BorderLayout.WEST);


        JButton permsButton = new JButton("Manage");
        channelManagePanel.add(permsButton, BorderLayout.EAST);

        // Permissions dialog popup
        permsButton.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            Frame owner = (w instanceof Frame) ? (Frame) w : null;
            PermissionsView dialog = new PermissionsView(owner);
            dialog.setVisible(true);
        });

        rightBox.add(channelManagePanel);
        rightBox.add(Box.createVerticalStrut(8));

        return rightBox;
    }

    /**
     * Returns the theme button for external theme management.
     */
    public ThemeButton getThemeButton() {
        return themeButton;
    }

    /**
     * Creates a standardized border for panels.
     */
    private Border borderBox() {
        return new CompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 90), 1),
                new EmptyBorder(4, 4, 4, 4)
        );
    }

    private void joinChannel(String channelId) {
        messageModel.clear();
        messageModel.addElement("=== Joined channel: " + channelId + " ===");
    }
}
