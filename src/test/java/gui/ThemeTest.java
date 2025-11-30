package gui;
import org.junit.Test;
import static org.junit.Assert.*;

public class ThemeTest {

    @Test
    public void themeTest() {
        ThemeInteractor themes = new ThemeInteractor(null);
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
