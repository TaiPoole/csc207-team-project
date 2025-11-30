package gui;

/** ThemeOutputBoundary interface.
 *  Defines a requirement to be able to change the buttons text, and paint the ui
 */
public interface ThemeOutputBoundary {

    /** Sets the button text.
     *
     * @param text text to set
     */
    void setText(String text);

    /** Paints the ui.
     *
     * @param palette palette for painting
     */
    void applyPalette(Palette palette);
}
