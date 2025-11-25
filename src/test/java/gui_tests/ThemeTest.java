package gui_tests;
import gui.Themes;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;

public class ThemeTest {

    @Test
    public void themeTest() {
        Themes themes = new Themes();
        themes.cyclePalette();
        assertEquals("Palettes not cycling correctly", "gui.Sakura", themes.theme.getClass().getName());
        themes.cyclePalette();
        assertEquals("Palettes not cycling correctly", "gui.Hacker", themes.theme.getClass().getName());
        themes.cyclePalette();
        assertEquals("Palettes not cycling correctly", "gui.Light", themes.theme.getClass().getName());
        themes.cyclePalette();
        assertEquals("Palettes not cycling correctly", "gui.Dark", themes.theme.getClass().getName());
    }
}
