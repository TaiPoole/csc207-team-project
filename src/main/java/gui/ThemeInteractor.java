package gui;

/** Themes class.
 * Organizes themes and handles logic between switching them in the GUI
 */
public class ThemeInteractor implements ThemeInputBoundary {
    public Palette theme;
    public ThemeOutputBoundary presenter;

    /** Basic constructor.
     *  Defaults the theme on init to dark mode (obviously)
     */
    public ThemeInteractor(ThemePresenter presenter) {
        this.theme = new Dark();
        this.presenter = presenter;
    }

    /** Cycles between themes.
     *  Order is Dark -> Sakura -> Hacker -> Light -> Dark
     */
    public void cyclePalette() {
        switch (this.theme.getClass().getName().substring(4)) {
            case "Sakura":
                this.theme = new Hacker();
                break;
            case "Hacker":
                this.theme = new Light();
                break;
            case "Dark":
                this.theme = new Sakura();
                break;
            default:
                this.theme = new Dark();
        }
        if (presenter != null) {
            presenter.setText(this.theme.getClass().getName().substring(4) + " Mode");
            presenter.applyPalette(this.theme);
        }
    }
}


