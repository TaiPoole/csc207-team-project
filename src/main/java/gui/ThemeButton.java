package gui;

import javax.swing.JPanel;

/** ThemeButton class.
 *  an extension of the JButton class for the button that changes the theme
 */
public class ThemeButton extends Button {
    private final Themes theme;

    /** ThemeButton constructor.
     *  makes the theme button as an extension of JButton
     *
     * @param s the string to pass as initial text
     */
    public ThemeButton(String s) {
        super(s);
        theme = new Themes();
    }

    public Themes getTheme() {
        return theme;
    }

    /** Create Environment.
     *  changes the theme and text, and applies the new theme palette
     *
     * @param mainPanel the mainPanel that holds everything to be painted
     */
    public void createEnvironment(JPanel mainPanel) {
        this.theme.cyclePalette();
        this.setText(theme.theme.getClass().getName().substring(4) + " Mode");
        theme.theme.applyPalette(mainPanel);
    }
}
