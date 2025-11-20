package GUI;

import javax.swing.*;
// Each button will be a subclass and add an other methods/ functionality needed
public class Button extends JButton {
    String text;

    public Button(){
        super();
    }
    public Button(String text){
        super(text);
    }
    public Button(Action action){
        super(action);
    }
}
