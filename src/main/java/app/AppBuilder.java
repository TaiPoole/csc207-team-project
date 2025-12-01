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
import client.searchmessage.SearchMessageController;
import client.searchmessage.SearchMessageInputBoundary;
import client.searchmessage.SearchMessageInteractor;
import client.searchmessage.SearchMessageOutputBoundary;
import client.searchmessage.SearchMessagePresenter;
import client.sendmessage.SendMessageInputBoundary;
import client.sendmessage.SendMessageInteractor;
import client.sendmessage.SendMessageOutputBoundary;
import client.sendmessage.SendMessagePresenter;
import common.RandomNameGenerator;
import gui.ThemeController;
import gui.ThemeInteractor;
import gui.ThemePresenter;
import interfaceadapter.ChatViewModel;
import interfaceadapter.RandomNameViewModel;
import interfaceadapter.SearchMessageViewModel;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import permissions.ManageMessagePresenter;
import permissions.ManagePermissionsInputBoundary;
import permissions.ManagePermissionsInteractor;
import permissions.ManagePermissionsOutputBoundary;
import permissions.ServerPermissionsGateway;
import view.MainView;
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

    // Clean Architecture components
    private final ReceiveMessageInputBoundary receiveInteractor;
    private final SendMessageInputBoundary sendMessageInteractor;
    private final ManagePermissionsInputBoundary permissionsInteractor;
    private final PermissionsView permissionsView;
    private final GenerateRandomNameOutputBoundary randomNamePresenter;
    private final GenerateRandomNameInputBoundary randomNameInteractor;
    private final GenerateRandomNameController randomNameController;
    private final SearchMessageInputBoundary searchMessageInteractor;
    private final SearchMessageViewModel searchMessageViewModel;
    private final SearchMessageController searchMessageController;

    private ChatViewModel chatViewModel;

    /**
     * Creates a new AppBuilder with a default application frame.
     */
    public AppBuilder() throws IOException {
        this.frame = new JFrame("TRIUMPH");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.randomNameGenerator = new RandomNameGenerator();
        this.messageModel = new DefaultListModel<>();
        this.randomNameViewModel = new RandomNameViewModel();

        final String defaultName = randomNameGenerator.generate();

        this.chatViewModel = new ChatViewModel(messageModel);
        this.chatViewModel.setActiveChannel("general");

        // Set up receive message chain first
        ReceiveMessageOutputBoundary receivePresenter = new ReceiveMessagePresenter(chatViewModel);
        this.receiveInteractor = new ReceiveMessageInteractor(receivePresenter);

        // Create client with message handler

        this.client = new Client(defaultName, "localhost", message -> {
            if (message != null) {
                ReceiveMessageInputData inputData = new ReceiveMessageInputData(message);
                String sender = message.getUsername();
                receiveInteractor.execute(inputData, sender);
            }
        });


        client.setCurrentChannel("general");


        SendMessageOutputBoundary sendPresenter = new SendMessagePresenter(chatViewModel);
        this.sendMessageInteractor = new SendMessageInteractor(sendPresenter, client);

        this.client.connect();

        this.permissionsView = new PermissionsView(this.frame);
        ManagePermissionsOutputBoundary managePresenter = new ManageMessagePresenter(permissionsView);
        ServerPermissionsGateway permissionGateway = new ServerPermissionsGateway(client.getConnection());
        this.permissionsInteractor = new ManagePermissionsInteractor(permissionGateway, managePresenter);

        // Set up generate random name chain
        this.randomNamePresenter =
                new GenerateRandomNamePresenter(randomNameViewModel);
        this.randomNameInteractor =
                new GenerateRandomNameInteractor(randomNameGenerator, randomNamePresenter);
        this.randomNameController =
                new GenerateRandomNameController(randomNameInteractor);

        // Set up search message chain
        this.searchMessageViewModel = new SearchMessageViewModel();
        SearchMessageOutputBoundary searchPresenter =
                new SearchMessagePresenter(searchMessageViewModel);
        this.searchMessageInteractor =
                new SearchMessageInteractor(chatViewModel, searchPresenter);
        this.searchMessageController =
                new SearchMessageController(searchMessageInteractor);
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
                chatViewModel,
                sendMessageInteractor,
                permissionsInteractor,
                permissionsView,
                searchMessageController,
                searchMessageViewModel
        );

        ThemePresenter themePresenter = new ThemePresenter(mainView.getThemeButton(), mainView);
        ThemeInteractor themeInteractor = new ThemeInteractor(themePresenter);
        ThemeController themeController = new ThemeController(themeInteractor);
        this.mainView.getThemeButton().setValues("Dark Mode", themeController);

        this.frame.setContentPane(mainView);

        // Apply initial theme
        themePresenter.applyPalette(themeInteractor.theme);

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