package gui;

import java.awt.Color;

/** Dark theme.
 *  Dark greys. Default Palette
 */
public class Dark extends Palette {
    /** Dark Theme constructor.
     *  Fills in preset template colours.
     */
    public Dark() {
        this.mainBg = new Color(32, 34, 37);
        this.panelBg = new Color(47, 49, 54);
        this.text = new Color(232, 232, 232);
        this.buttonBg = new Color(19, 19, 19);
        this.buttonText = new Color(255, 255, 255);
    }
}
