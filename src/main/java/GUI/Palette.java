package GUI;

import javax.swing.*;
import java.awt.*;

//Holds colour information
public abstract class Palette {
    public Color mainBg;
    public Color panelBg;
    public Color text;
    public Color buttonBg;
    public Color buttonText;


    // applies a palette recursively, using the themes set by the abstract
    public void applyPalette(Component c) {
        String o = c.getClass().getName().substring(12);
        switch (o) {
            case "JButton":
                ((JButton) c).setContentAreaFilled(false);
                ((JButton) c).setOpaque(true);
                c.setBackground(this.buttonBg);
                c.setForeground(this.buttonText);
                break;
            case "JPanel":
                c.setBackground(this.panelBg);
                c.setForeground(this.text);
                break;
            case "JScrollPane":
                c.setBackground(this.panelBg);
                c.setForeground(this.text);
                ((JScrollPane) c).getViewport().setBackground(this.panelBg);
                break;
            default:
                c.setBackground(this.mainBg);
                c.setForeground(this.text);
                break;
        }
        if  (c instanceof JComponent) {
            for (Component child : ((JComponent) c).getComponents()) {
                applyPalette(child);
            }
        }
    }
}


