package ru.otus.homework.lastProject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Unit implements Settings{
    protected int x;
    protected int y;
    protected int height;
    protected int width;
    protected GamePane gamePane;
    protected Actions action;
    protected BufferedImage img;

    Unit(GamePane gamePane) {
        this.gamePane = gamePane;
        int x = 0;
        int y = 0;
    }

    Unit(GamePane gamePane, String img, int x, int y) {
        this.gamePane = gamePane;
        this.x = x;
        this.y = y;

        try {
            this.img = ImageIO.read(new File(getClass().getResource(img).getFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Draw (Graphics g) {}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getLX() {
        return x;
    }

    public int getRX() {
        return x + width;
    }

    public int getTY() {
        return y;
    }

    public int getBY() {
        return y + height;
    }

    public Actions getAction() {
        return this.action;
    }
}
