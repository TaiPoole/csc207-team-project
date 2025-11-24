package gui;

/** Themes class.
 * Organizes themes and handles logic between switching them in the GUI
 */
public class Themes {
    public Palette theme;

    /** Basic constructor.
     *  Defaults the theme on init to dark mode (obviously)
     */
    public Themes() {
        this.theme = new Dark();
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
    }
}


