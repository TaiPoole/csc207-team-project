package gui;

/** ThemeButton class.
 *  an extension of the JButton class for the button that changes the theme
 */
public class ThemeButton extends Button {
    private ThemeController themeController;

    /** Empty constructor.
     *  Since theming needs a reference to the full frame to paint,
     *  and theme button has to be placed AS the frame is being made,
     *  we make an empty constructor and set the values later
     */
    public ThemeButton() {}

    /** Populates the necessary values (basically the constructor).
     *
     * @param s string to initialize with
     * @param themeController controller for it to hold
     */
    public void setValues(String s, ThemeController themeController) {
        this.setText(s);
        this.themeController = themeController;
        addActionListener(e -> this.themeController.changeTheme());
    }
}
