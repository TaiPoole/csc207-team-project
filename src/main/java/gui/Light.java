package gui;

import java.awt.Color;

/** Light theme.
 *  Light greys. Don't use light mode that's crazy
 */
class Light extends Palette {
    public Light() {
        this.mainBg = new Color(255, 255, 255);
        this.panelBg = new Color(153, 153, 153);
        this.text = new Color(61, 61, 61);
        this.buttonBg = new Color(47, 47, 47);
        this.buttonText = new Color(255, 255, 255);
    }
}
