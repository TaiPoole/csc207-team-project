package gui;

/** ThemeController class.
 *  Handles starting the theme changing process
 */
public class ThemeController {
    public ThemeInputBoundary interactor;

    /** Basic Constructor.
     *
     * @param interactor interactor to set
     */
    public ThemeController(ThemeInteractor interactor) {
        this.interactor = interactor;
    }

    /** Uses the interactor to initiate changing the theme.
     *
     */
    public void changeTheme() {
        this.interactor.cyclePalette();
    }
}
