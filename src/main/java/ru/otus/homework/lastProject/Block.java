package ru.otus.homework.lastProject;

import java.awt.*;

public class Block extends Unit {
    private boolean isKicked;
    private int kickHeight;
    private int kickCurrentHeight;

    Block(GamePane gamePane, int x, int y) {
        super(gamePane, "/block.png", x, y);
        this.width = 50;
        this.height = 50;

        this.kickHeight = this.height/2;
        this.kickCurrentHeight = 0;
        this.action = Actions.STAND;
    }

    public void Draw (Graphics g) {
        if (this.action == Actions.KICKED) {
            if (kickCurrentHeight < kickHeight) {
                kickCurrentHeight += KICK_SPEED_TOP;
                this.y -= KICK_SPEED_TOP;
            }
            else {
                this.action = Actions.FALL;
            }
        }

        if (this.action == Actions.FALL) {
            if(kickCurrentHeight > 0) {
                kickCurrentHeight -= KICK_SPEED_BOTTOM;
                this.y += KICK_SPEED_BOTTOM;
            }
            else {
                this.action = Actions.STAND;
                this.isKicked = false;
            }
        }

        Image subSprite;
        subSprite = img.getSubimage(0, 0, width, height);
        g.drawImage(subSprite, x, y, null);
    }

    public void setIsKicked(boolean isKicked) {
        this.isKicked = isKicked;
        this.action = Actions.KICKED;
    }

    public boolean getIsKicked() {
        return isKicked;
    }
}
