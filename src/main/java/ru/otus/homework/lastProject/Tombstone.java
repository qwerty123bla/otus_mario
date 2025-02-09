package ru.otus.homework.lastProject;

import java.awt.*;

public class Tombstone extends Unit {

    Tombstone(GamePane gamePane, int x, int y) {
        super(gamePane, "/tombstone.png", x, y);
        this.width = 100;
        this.height = 100;

        this.action = Actions.FALL;
    }

    public void Draw (Graphics g) {
        boolean onBlock = false;
        if (action == Actions.FALL) {
            for(Block b: this.gamePane.getBlocks()) {
                if ((this.x >= b.getX() && this.x <= b.getX() + b.getWidth() || this.x + this.width >= b.getX() && this.x + this.width <= b.getX() + b.getWidth())
                        && Math.abs(this.y + this.height - b.getY()) <= ENEMY_FALL_SPEED ) {
                    onBlock = true;
                    if (action == Actions.FALL)  this.y = b.getY() - this.height;

                }
            }

            if ((onBlock || this.y + this.height >= this.gamePane.getHeight()) && (this.action == Actions.FALL || this.action == Actions.KICKED_FALL)) {
                this.action = Actions.STAND;
                gamePane.setGameLifeCycle(GameLifeCycle.END);
            }
            else {
                y += ENEMY_FALL_SPEED;
            }
        }

        Image subSprite;
        subSprite = img.getSubimage(0, 0, width, height);
        g.drawImage(subSprite, x, y, null);
    }
}
