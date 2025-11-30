package view;

import client.Client;
import client.generatename.GenerateRandomNameController;
import client.sendmessage.SendMessageController;
import client.sendmessage.SendMessageInputBoundary;
import common.RandomNameGenerator;
import common.Attachment;
import interfaceadapter.AttachmentRegistry;
import interfaceadapter.RandomNameViewModel;
import gui.PickFileListener;
import gui.SendButtonListener;
import gui.ThemeButton;
import permissions.ManagePermissionsInputBoundary;
import permissions.ManagePermissionsInteractor;
import permissions.PermissionsController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.io.IOException;

/**
 * The main messaging UI.
 */
public class MainView extends JPanel {

    private final Client client;
    private final GenerateRandomNameController generateRandomNameController;
    private final RandomNameViewModel randomNameViewModel;
    private final SendMessageInputBoundary sendMessageInteractor;
    private final ManagePermissionsInputBoundary permissionsInteractor;

    // ListModels
    private final DefaultListModel<String> channelModel = new DefaultListModel<>();
    private final DefaultListModel<String> messageModel;
    private final AttachmentRegistry attachmentRegistry;

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
    private PermissionsController permissionsController;
    private PermissionsView permissionsView;

    /**
     * Constructs the main messaging UI layout.
     */
    public MainView(
            Client client,
            GenerateRandomNameController generateRandomNameController,
            RandomNameViewModel randomNameViewModel,
            DefaultListModel<String> messageModel,
            SendMessageInputBoundary sendMessageInteractor,
            AttachmentRegistry attachmentRegistry
            ManagePermissionsInputBoundary permissionsInteractor,
            PermissionsView permissionsView
    ) {
        this.client = client;
        this.generateRandomNameController = generateRandomNameController;
        this.randomNameViewModel = randomNameViewModel;
        this.messageModel = messageModel;
        this.sendMessageInteractor = sendMessageInteractor;
        this.attachmentRegistry = attachmentRegistry;
        this.permissionsInteractor = permissionsInteractor;
        this.permissionsView = permissionsView;

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

        // Double-click a message with an attachment to download it
        messageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = messageList.locationToIndex(e.getPoint());
                    if (index < 0) {
                        return;
                    }

                    Attachment attachment = attachmentRegistry.getAttachment(index);
                    if (attachment == null) {
                        // No file attached
                        return;
                    }

                    JFileChooser chooser = new JFileChooser();
                    chooser.setSelectedFile(new File(attachment.getName()));
                    int result = chooser.showSaveDialog(MainView.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File target = chooser.getSelectedFile();
                        try (FileOutputStream out = new FileOutputStream(target)) {
                            out.write(attachment.getAttachment());
                            out.flush();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(
                                    MainView.this,
                                    "Failed to save file: " + ex.getMessage(),
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
        });

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
            //filePicker = new PickFileListener(owner, fileDisplayPanel);
            //use the same filePicker
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
            generateRandomNameController.generateRandomName();
            String newName = randomNameViewModel.getLatestName();
            client.setUsername(newName);       // update local identity
            usernameField.setText(newName);    // reflect in UI
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
        // "+" Add Channel button
        JButton addChannelButton = new JButton("+");
        channelManagePanel.add(addChannelButton, BorderLayout.WEST);
        JButton permsButton = new JButton("Manage");
        channelManagePanel.add(permsButton, BorderLayout.EAST);

        // Add Channel dialog popup
        addChannelButton.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            Frame owner = (w instanceof Frame) ? (Frame) w : null;

            AddChannelDialog dialog = new AddChannelDialog(owner, channelModel, client);
            dialog.setVisible(true);
        });

        // Permissions dialog popup
        permsButton.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            Frame owner = (w instanceof Frame) ? (Frame) w : null;

            if (permissionsController == null && owner != null) {
                permissionsController = new PermissionsController(permissionsView, permissionsInteractor);
            }

            if (permissionsController != null) {
                permissionsController.show();
            }
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
}