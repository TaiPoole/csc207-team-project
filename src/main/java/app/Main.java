package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
            AppBuilder builder = new AppBuilder();
            JFrame frame = builder
                    .addMainView()
                    .build();

            frame.setVisible(true);
        });
    }
}