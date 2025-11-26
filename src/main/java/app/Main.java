package app;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * The entry point of the application.
 */
public class Main {
    /**
     * Launches the application.
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            AppBuilder builder;
            try {
                builder = new AppBuilder();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            builder.setLookAndFeel();
            JFrame frame = builder
                    .addMainView()
                    .build();

            frame.setVisible(true);
        });
    }
}