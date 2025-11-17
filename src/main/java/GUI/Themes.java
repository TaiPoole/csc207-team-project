package GUI;

import java.awt.*;

// Selector for themes
public class Themes {
    public Palette theme;

    public Themes() {
        this.theme = new Dark();
    }

    //This is for if you want to have a dropdown box Yuki
    public void setPalette(String themeName) {
        switch (themeName) {
            case "Light":
                this.theme = new Light();
            case "Sakura":
                this.theme = new Sakura();
            case "Hacker":
                this.theme = new Hacker();
            case "Dark":
            default:
                this.theme = new Dark();
        }
    }

    //Cycles to the next theme
    public void cyclePalette() {
        switch (this.theme.getClass().getName().substring(4)) {
            case "Light":
                this.theme = new Dark();
                break;
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


//Button backgrounds are all fucked up idk its an OS override things who fucking knows TODO fix
class Light extends Palette {
    public Light() {
        this.mainBg = new Color(255, 255, 255);
        this.panelBg = new Color(153, 153, 153);
        this.text = new Color(61, 61, 61);
        this.buttonBg = new Color(47, 47, 47);
        this.buttonText = new Color(255, 255, 255);
    }
}

class Dark extends Palette {
    public Dark() {
        this.mainBg = new Color(32, 34, 37);
        this.panelBg = new Color(47, 49, 54);
        this.text = new Color(232, 232, 232);
        this.buttonBg = new Color(19, 19, 19);
        this.buttonText = new Color(255, 255, 255);
    }
}

//same as dark for now ill do it later
class Sakura extends Palette {
    public Sakura() {
        this.mainBg = new Color(253, 247, 250);
        this.panelBg = new Color(246, 230, 236);
        this.text = new Color(74, 58, 65);
        this.buttonBg = new Color(235, 167, 192);
        this.buttonText = new Color(255, 255, 255);
    }
}

class Hacker extends Palette {
    public Hacker() {
        this.mainBg = new Color(0, 0, 0);
        this.panelBg = new Color(11, 15, 11);
        this.text = new Color(57, 255, 20);
        this.buttonBg = new Color(0, 200, 83);
        this.buttonText = new Color(255, 255, 255);
    }
}