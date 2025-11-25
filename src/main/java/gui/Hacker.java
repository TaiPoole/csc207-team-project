package gui;

import java.awt.Color;

/** Hacker theme.
 *  Black and green colours like the hackers in movies
 */
class Hacker extends Palette {
    public Hacker() {
        this.mainBg = new Color(0, 0, 0);
        this.panelBg = new Color(11, 15, 11);
        this.text = new Color(57, 255, 20);
        this.buttonBg = new Color(0, 200, 83);
        this.buttonText = new Color(255, 255, 255);
    }
}
