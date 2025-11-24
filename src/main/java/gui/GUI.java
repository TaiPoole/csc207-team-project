package gui;

import client.Client;
import client.receivemessage.*;
import client.sendmessage.*;
import common.RandomNameGenerator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUI {

    private static Border borderBox() {
        return new CompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 90, 90), 1),
                new EmptyBorder(4, 4, 4, 4)
        );
    }

    //Notice about the buttons: we will have to make them their own variables since we have to add action listeners to them
    public static void main(final String[] args) {

        JFrame frame = new JFrame("The really cool messaging service");

        RandomNameGenerator nameGenerator = new RandomNameGenerator();
        String initialName = "User";
      
        // moved messageModel initialisation to the top for the ReceiveMessage
        DefaultListModel<String> messageModel = new DefaultListModel<>();

        ReceiveMessageOutputBoundary receivePresenter = new ReceiveMessagePresenter(messageModel);
        ReceiveMessageInputBoundary receiveInteractor = new ReceiveMessageInteractor(receivePresenter);

//        Client client = new Client(initialName, "localhost", (message -> {
//            if (message != null) {
//                ReceiveMessageInputData inputData = new ReceiveMessageInputData(message);
//                receiveInteractor.execute(inputData);
//            }
//        }));

        Client client = new Client(initialName, "localhost", message -> {
            if (message == null) {
                return;
            }
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
                return;
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
                return;
            }

            ReceiveMessageInputData inputData = new ReceiveMessageInputData(message);
            receiveInteractor.execute(inputData);
        });


        try {
            client.connect();
            client.sendMessage("test");
        } catch (
                Exception e
        ) {
            System.err.println(e);
        }

        //Specific thing necessary for the button styling
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            //JFrame frame = new JFrame("The really cool messaging service");

            // Right "half" (top-bottom) : settingsPanel, channelSearchPanel, channelListScroll, channelManagePanel
            JPanel rightBox = new JPanel();
            rightBox.setLayout(new BoxLayout(rightBox, BoxLayout.Y_AXIS));
            rightBox.setPreferredSize(new Dimension(400, 800));

            // settingsPanel
            JPanel settingsPanel = new JPanel();
            settingsPanel.setPreferredSize(new Dimension(800, 80));
            settingsPanel.setLayout(new BorderLayout(5, 5));
            settingsPanel.setBorder(borderBox());
            JTextField usernameField = new JTextField(initialName);
            settingsPanel.add(usernameField, BorderLayout.WEST);
            JButton newButton = new JButton("New");
            settingsPanel.add(newButton, BorderLayout.CENTER);
            JButton themeButton = new JButton("Dark");
            settingsPanel.add(themeButton, BorderLayout.EAST);
            rightBox.add(settingsPanel);
            rightBox.add(Box.createVerticalStrut(8));

            // channelSearchPanel
            JPanel channelSearchPanel = new JPanel();
            channelSearchPanel.setPreferredSize(new Dimension(800, 80));
            channelSearchPanel.setLayout(new BorderLayout(5, 5));
            channelSearchPanel.setBorder(borderBox());
            channelSearchPanel.add(new JTextField("Channel ID:"), BorderLayout.CENTER);
            channelSearchPanel.add(new JButton("Join"), BorderLayout.EAST);
            rightBox.add(channelSearchPanel);
            rightBox.add(Box.createVerticalStrut(8));

            // channelListScroll
            DefaultListModel<String> channelModel = new DefaultListModel<>();
            channelModel.addElement("# this is a channel");
            channelModel.addElement("# this is a channel as well");
            channelModel.addElement("# ok another channel");
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
            //don't really need this since both add channel
            // and manage permission has channel name in the dialog
            //channelManagePanel.add(new JTextField("Name:"), BorderLayout.CENTER);

            AddChannelButton plusButton = new AddChannelButton(frame, client);
            channelManagePanel.add(plusButton);


            JButton permsButton = new JButton("Manage");
            permsButton.addActionListener(e -> {
                PermissionsDialog dialog = new PermissionsDialog(frame);
                dialog.setVisible(true);
            });
            channelManagePanel.add(permsButton, BorderLayout.EAST);

            rightBox.add(channelManagePanel);
            rightBox.add(Box.createVerticalStrut(8));

            // Left "half" (top-bottom): searchPanel, messageScroll, sendPanel
            JPanel leftBox = new JPanel();
            leftBox.setLayout(new BoxLayout(leftBox, BoxLayout.Y_AXIS));
            leftBox.setPreferredSize(new Dimension(800, 800));

            // searchPanel
            JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
            searchPanel.setPreferredSize(new Dimension(800, 80));
            searchPanel.setBorder(borderBox());
            searchPanel.add(new JTextField("Search:"), BorderLayout.CENTER);
            searchPanel.add(new JButton("Go"), BorderLayout.EAST);
            leftBox.add(searchPanel);
            leftBox.add(Box.createVerticalStrut(8));

            // messageScroll // IDK how the messages would be displayed so I just make it a Scroll-like right now.
            // moved messageModel initialisation to the top for the ReceiveMessage
            messageModel.addElement("THIS IS WHERE THE MESSAGES GO");
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
            JButton sendButton = new JButton("Send");
            JTextField messageField = new JTextField();
            sendPanel.add(new JButton("Add File"), BorderLayout.WEST);
            sendPanel.add(messageField, BorderLayout.CENTER);
            sendPanel.add(sendButton, BorderLayout.EAST);
            leftBox.add(sendPanel);

            //Main Panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            mainPanel.add(leftBox);
            mainPanel.add(Box.createHorizontalStrut(8));
            mainPanel.add(rightBox);

            //Palette and theming stuff
            //Update from Tiger: I added createEnvironment inside ThemeButton, which can replace everything in action listener
            Themes theme = new Themes();
            theme.theme.applyPalette(mainPanel);
            themeButton.addActionListener(e -> {
                theme.cyclePalette();
                themeButton.setText(theme.theme.getClass().getName().substring(4));
                theme.theme.applyPalette(mainPanel);
            });

            newButton.addActionListener(e -> {
                String newName = nameGenerator.generate();
                client.setUsername(newName);       // update local identity
                usernameField.setText(newName);    // reflect in UI
            });
            // Send message listener
            SendMessageOutputBoundary presenter = new SendMessagePresenter(messageModel);
            SendMessageInputBoundary sendMessageInteractor = new SendMessageInteractor(presenter, client);
            SendMessageController sendMessageController = new SendMessageController(sendMessageInteractor);
            sendButton.addActionListener(e -> {
                String message = messageField.getText().trim();
                sendMessageController.sendMessage(message);
                messageField.setText("");
            });

            // Allow Enter key to send message
            messageField.addActionListener(e -> {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    sendMessageController.sendMessage(message);
                    messageField.setText("");
                }
            });

            // Main Frame
            //TODO please don't let this be the final name
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
