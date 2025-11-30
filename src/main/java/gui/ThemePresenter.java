package gui;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/** ThemePresenter class.
 *  In charge of painting the themes to the UI
 */
public class ThemePresenter implements ThemeOutputBoundary {
    private final ThemeButton button;
    private final JPanel mainPanel;

    /** Basic constructor.
     *
     * @param button theme button to change the theme text
     * @param panel main panel to paint
     */
    public ThemePresenter(ThemeButton button, JPanel panel) {
        this.button = button;
        this.mainPanel = panel;
    }

    /** First part of painting.
     *  This one takes the main pane, and then recursively calls paint on subcomponents
     *
     * @param palette palette to paint
     */
    public void applyPalette(Palette palette) {
        mainPanel.setBackground(palette.mainBg);
        mainPanel.setForeground(palette.text);
        for (Component child : this.mainPanel.getComponents()) {
            applyPalette(palette, child);
        }
    }

    /** Second part of painting.
     *  Works the same as above, just with a recursive argument
     *
     * @param palette palette to paint
     */
    public void applyPalette(Palette palette, Component c) {
        String o = c.getClass().getName().substring(12);
        switch (o) {
            case "JButton":
                ((JButton) c).setContentAreaFilled(false);
                ((JButton) c).setOpaque(true);
                c.setBackground(palette.buttonBg);
                c.setForeground(palette.buttonText);
                break;
            case "JPanel":
                c.setBackground(palette.panelBg);
                c.setForeground(palette.text);
                break;
            case "JScrollPane":
                c.setBackground(palette.panelBg);
                c.setForeground(palette.text);
                ((JScrollPane) c).getViewport().setBackground(palette.panelBg);
                break;
            default:
                c.setBackground(palette.mainBg);
                c.setForeground(palette.text);
                break;
        }
        if (c instanceof JComponent) {
            for (Component child : ((JComponent) c).getComponents()) {
                applyPalette(palette, child);
            }
        }
    }

    /** Sets the buttons text to '{theme} Mode'.
     *
     * @param text text to set the button to
     */
    public void setText(String text) {
        this.button.setText(text);
    }
}
