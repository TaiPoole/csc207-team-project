package app;

import client.Client;
import client.receivemessage.*;
import client.sendmessage.*;
import common.RandomNameGenerator;
import javax.swing.*;

import gui.ThemeButton;
import view.MainView;

import java.io.IOException;

/**
 * Builder for initializing and configuring the main application frame.
 */
public class AppBuilder {

    private final JFrame frame;
    private MainView mainView;

    private final Client client;
    private final RandomNameGenerator nameGenerator;
    private final DefaultListModel<String> messageModel;

    // Clean Architecture components
    private ReceiveMessageInputBoundary receiveInteractor;
    private SendMessageInputBoundary sendMessageInteractor;

    /**
     * Creates a new AppBuilder with a default application frame.
     */
    public AppBuilder() throws IOException {
        this.frame = new JFrame("The really cool messaging service");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.nameGenerator = new RandomNameGenerator();
        this.messageModel = new DefaultListModel<>();

        String defaultName = "User";

        // Set up receive message chain first
        ReceiveMessageOutputBoundary receivePresenter = new ReceiveMessagePresenter(messageModel);
        this.receiveInteractor = new ReceiveMessageInteractor(receivePresenter);

        // Create client with message handler
        this.client = new Client(defaultName, "localhost", message -> {
            if (message != null) {
                ReceiveMessageInputData inputData = new ReceiveMessageInputData(message);
                receiveInteractor.execute(inputData);
            }
        });

        // Set up send message chain
        SendMessageOutputBoundary sendPresenter = new SendMessagePresenter(messageModel);
        this.sendMessageInteractor = new SendMessageInteractor(sendPresenter, client);

        this.client.connect();
    }

    /**
     * Sets the UI Look and Feel.
     */
    public AppBuilder setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Adds the MainView to the application frame.
     */
    public AppBuilder addMainView() {
        this.mainView = new MainView(
                client,
                nameGenerator,
                messageModel,
                sendMessageInteractor
        );

        this.frame.setContentPane(mainView);

        // Apply initial theme
        ThemeButton themeButton = mainView.getThemeButton();
        themeButton.getTheme().theme.applyPalette(mainView);

        // Set up theme change listener
        themeButton.addActionListener(e -> {
            themeButton.createEnvironment(mainView);
        });

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