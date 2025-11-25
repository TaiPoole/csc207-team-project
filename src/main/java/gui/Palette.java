package gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

/** Palette class.
 *  Synonymous with theme, this is the class that holds theme colours and applies them to the GUI
 */
public abstract class Palette {
    public Color mainBg;
    public Color panelBg;
    public Color text;
    public Color buttonBg;
    public Color buttonText;

    /** applyPalette. Applies the current palette to passed in component
     *  This function starts by passing in the mainPanel in our GUI, and recurses to colour in every sub-panel
     *
     * @param c component to be coloured (and/or recursed to access more components).
     */
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
        if (c instanceof JComponent) {
            for (Component child : ((JComponent) c).getComponents()) {
                applyPalette(child);
            }
        }
    }
}


