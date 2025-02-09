package ru.otus.homework.lastProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainPane extends JPanel implements KeyListener {
    GamePane gamePane;
    //private JToolBar jtbEdit;

    MainPane() {
        setOpaque(true);
        setLayout(new BorderLayout());

        this.gamePane = new GamePane();
        this.gamePane.addKeyListener(this.gamePane);
        this.gamePane.setFocusable(true);
        this.gamePane.setFocusable(true);
        add(gamePane, BorderLayout.CENTER);

        //JToolBar jtb = new JToolBar("bla");
        //jtb.setFloatable(false);
        gamePane.play();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 39) {
            // right
            gamePane.toRight();
        }
        else if (e.getKeyCode() == 37) {
            // left
            gamePane.toLeft();
        }
        else if (e.getKeyCode() == 32) {
            gamePane.jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 39 || e.getKeyCode() == 37) {
            gamePane.releaseKey();
        }
    }
}
