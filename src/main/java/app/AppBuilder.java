
package app;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import view.MainView;

/**
 * Builder for initializing and configuring the main application frame.
 */
public class AppBuilder {

    private final JFrame frame;
    private MainView mainView;

    /**
     * Creates a new AppBuilder with a default application frame.
     */
    public AppBuilder() {
        this.frame = new JFrame("The really cool messaging service");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Adds the MainView to the application frame.
     */
    public AppBuilder addMainView() {
        this.mainView = new MainView();

        this.frame.setContentPane(mainView);

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