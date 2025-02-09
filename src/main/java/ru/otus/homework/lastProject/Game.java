package ru.otus.homework.lastProject;

import javax.swing.*;

public class Game implements Settings{
    MainPane mainPane;
    Game() {
        JFrame jfrm = new JFrame("Mario");
        jfrm.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrm.setResizable(false);
        jfrm.setVisible(true);

        this.mainPane = new MainPane();
        this.mainPane.addKeyListener(this.mainPane);
        this.mainPane.setFocusable(true);

        jfrm.setContentPane(mainPane);
    }
}
