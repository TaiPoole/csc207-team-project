package gui;
import javax.swing.*;

public class ThemeButton extends Button{
    public ThemeButton(String s) {
        super(s);
    }

    public void createEnvironment(JPanel mainPanel){
        Themes theme = new Themes();
        theme.cyclePalette();
        this.setText(theme.theme.getClass().getName().substring(4));
        theme.theme.applyPalette(mainPanel);
    }
}
