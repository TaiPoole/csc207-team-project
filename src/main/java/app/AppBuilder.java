package app;

import client.Client;
import client.generatename.GenerateRandomNameController;
import client.generatename.GenerateRandomNameInputBoundary;
import client.generatename.GenerateRandomNameInteractor;
import client.generatename.GenerateRandomNameOutputBoundary;
import client.generatename.GenerateRandomNamePresenter;
import client.receivemessage.ReceiveMessageInputBoundary;
import client.receivemessage.ReceiveMessageInputData;
import client.receivemessage.ReceiveMessageInteractor;
import client.receivemessage.ReceiveMessageOutputBoundary;
import client.receivemessage.ReceiveMessagePresenter;
import client.sendmessage.SendMessageInputBoundary;
import client.sendmessage.SendMessageInteractor;
import client.sendmessage.SendMessageOutputBoundary;
import client.sendmessage.SendMessagePresenter;
import common.RandomNameGenerator;
import gui.ThemeButton;
import permissions.*;
import interfaceadapter.RandomNameViewModel;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import view.MainView;
import interfaceadapter.AttachmentRegistry;

import view.PermissionsView;

/**
 * Builder for initializing and configuring the main application frame.
 */
public class AppBuilder {

    private final JFrame frame;
    private MainView mainView;

    private final Client client;
    private final RandomNameGenerator randomNameGenerator;
    private final DefaultListModel<String> messageModel;
    private final RandomNameViewModel randomNameViewModel;
    private final AttachmentRegistry attachmentRegistry;


    // Clean Architecture components
    private final ReceiveMessageInputBoundary receiveInteractor;
    private final SendMessageInputBoundary sendMessageInteractor;
    private final ManagePermissionsInputBoundary permissionsInteractor;
    private final PermissionsView permissionsView;
    private final GenerateRandomNameOutputBoundary randomNamePresenter;
    private final GenerateRandomNameInputBoundary randomNameInteractor;
    private final GenerateRandomNameController randomNameController;

    /**
     * Creates a new AppBuilder with a default application frame.
     */
    public AppBuilder() throws IOException {
        this.frame = new JFrame("The really cool messaging service");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.randomNameGenerator = new RandomNameGenerator();
        this.messageModel = new DefaultListModel<>();
        this.randomNameViewModel = new RandomNameViewModel();
        this.attachmentRegistry = new AttachmentRegistry();


        String defaultName = "User";

        // Set up receive message chain first
        ReceiveMessageOutputBoundary receivePresenter =
                new ReceiveMessagePresenter(messageModel, attachmentRegistry);
        this.receiveInteractor = new ReceiveMessageInteractor(receivePresenter);

        // Create client with message handler
        this.client = new Client(defaultName, "localhost", message -> {
            if (message != null) {
                ReceiveMessageInputData inputData = new ReceiveMessageInputData(message);
                receiveInteractor.execute(inputData);
            }
        });

        SendMessageOutputBoundary sendPresenter = new SendMessagePresenter(messageModel, attachmentRegistry);
        this.sendMessageInteractor = new SendMessageInteractor(sendPresenter, client);

        this.permissionsView = new PermissionsView(this.frame);
        ManagePermissionsOutputBoundary managePresenter = new ManageMessagePresenter(permissionsView);
        ServerPermissionsGateway permissionGateway = new ServerPermissionsGateway(client.getConnection());
        this.permissionsInteractor = new ManagePermissionsInteractor(permissionGateway, managePresenter);
        // Generate random name chain
        this.randomNamePresenter =
                new GenerateRandomNamePresenter(randomNameViewModel);
        this.randomNameInteractor =
                new GenerateRandomNameInteractor(randomNameGenerator, randomNamePresenter);
        this.randomNameController =
                new GenerateRandomNameController(randomNameInteractor);

        this.client.connect();
    }

    /**
     * Sets the UI Look and Feel.
     */
    public void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to reset default OS button formatting");
        }
    }

    /**
     * Adds the MainView to the application frame.
     */
    public AppBuilder addMainView() {
        this.mainView = new MainView(
                client,
                randomNameController,
                randomNameViewModel,
                messageModel,
                sendMessageInteractor,
                attachmentRegistry
                permissionsInteractor,
                permissionsView
        );

        this.frame.setContentPane(mainView);

        // Apply initial theme
        ThemeButton themeButton = mainView.getThemeButton();
        themeButton.getTheme().theme.applyPalette(mainView);

        // Set up theme change listener
        themeButton.addActionListener(e ->
                themeButton.createEnvironment(mainView)
        );

        return this;
    }

    /**
     * Finalizes configuration and returns the frame.
     */
    public JFrame build() {
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        return frame;
    }
}