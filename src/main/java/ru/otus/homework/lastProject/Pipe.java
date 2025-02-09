package ru.otus.homework.lastProject;

import java.awt.*;

public class Pipe extends Unit {
    Pipe(GamePane gamePane, String img, int x, int y, int width, int height) {
        super(gamePane, img, x, y);

        this.width = width;
        this.height = height;
    }

    public void Draw (Graphics g) {
        Image subSprite;
        subSprite = img.getSubimage(0, 0, this.width, this.height);
        g.drawImage(subSprite, x, y, null);
    }
}
