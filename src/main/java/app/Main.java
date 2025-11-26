package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.io.IOException;

/**
 * The entry point of the application.
 */
public class Main {
    /**
     * Launches the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            AppBuilder builder = null;
            try {
                builder = new AppBuilder();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JFrame frame = builder
                    .addMainView()
                    .build();

            frame.setVisible(true);
        });
    }
}