package app;

import view.MainView;

import javax.swing.*;

public class AppBuilder {

    private final JFrame frame;
    private MainView mainView;

    public AppBuilder() {
        this.frame = new JFrame("The really cool messaging service");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public AppBuilder addMainView() {
        this.mainView = new MainView();

        this.frame.setContentPane(mainView);

        return this;
    }

    public JFrame build() {
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        return frame;
    }
}