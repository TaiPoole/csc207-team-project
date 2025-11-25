
package app;

import client.Client;
import common.RandomNameGenerator;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import gui.ThemeButton;
import view.MainView;

/**
 * Builder for initializing and configuring the main application frame.
 */
public class AppBuilder {

    private final JFrame frame;
    private MainView mainView;

    private final Client client;
    private final RandomNameGenerator nameGenerator;

    /**
     * Creates a new AppBuilder with a default application frame.
     */
    public AppBuilder() {
        // TODO: PLEASE CHANGE THE NAME
        this.frame = new JFrame("The really cool messaging service");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.nameGenerator = new RandomNameGenerator();
        String defaultName = "User";
        this.client = new Client(defaultName, "localhost", message -> {
            System.out.println(message.getUsername() + ": " + message.getContent());
        }
        );
    }

    /**
     * Adds the MainView to the application frame.
     */
    public AppBuilder addMainView() {
        this.mainView = new MainView(client, nameGenerator);

        this.frame.setContentPane(mainView);

        // behaviour related to theming and changing themes
        ThemeButton themeButton = mainView.getThemeButton();
        themeButton.getTheme().theme.applyPalette(mainView);
        themeButton.addActionListener(e -> {
            themeButton.createEnvironment(mainView);
        });

        return this;
    }

    /**
     * Finalizes configuration and returns.
     */
    public JFrame build() {
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        return frame;
    }
}