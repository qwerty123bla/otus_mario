package ru.otus.homework.lastProject;

import java.awt.*;

public class Turtle extends Unit {
    protected int runActionCnt;
    protected int currentActionCnt;
    private int subSpritePos;
    private Actions actionBeforeFall;

    private int kickTime;
    private int currentKickTime;
    private int kickHeight;
    private int kickCurrentHeight;

    private int deathSpeed;

    Turtle(GamePane gamePane, String img, int x, int y, Actions action) {
        super(gamePane, img, x, y);
        this.width = 70;
        this.height = 100;

        this.runActionCnt = RUN_IMG_REPLACE_TIME;
        this.currentActionCnt = 0;
        this.subSpritePos = 0;
        this.action = action;

        kickTime = 400;
        currentKickTime = 0;

        this.kickHeight = this.height/2;
        this.kickCurrentHeight = 0;

        this.deathSpeed = 2;
    }

    public void calcReplaceImgWalk() {
        if (this.currentActionCnt < this.runActionCnt) {
            this.currentActionCnt++;
        }
        else  {
            this.currentActionCnt = 0;
            if (this.subSpritePos == 0) this.subSpritePos = 1; else this.subSpritePos = 0;
        }
    }

    public void Draw (Graphics g) {
        Image subSprite;
        int[][] spriteSheetCordsLeft = { { 0, 0, 70, 100 }, { 70, 0, 70, 100 } };
        int[][] spriteSheetCordsRight = { { 280, 0, 70, 100 }, { 350, 0, 70, 100 } };
        int[][] spriteSheetCordsHide = { { 140, 0, 70, 100 }, { 210, 0, 70, 100 } };

        if (this.action == Actions.LEFT) {
            // Влево
            x -= ENEMY_RUN_SPEED;
            calcReplaceImgWalk();
            subSprite = img.getSubimage(spriteSheetCordsLeft[this.subSpritePos][0], spriteSheetCordsLeft[this.subSpritePos][1], spriteSheetCordsLeft[this.subSpritePos][2], spriteSheetCordsLeft[this.subSpritePos][3]);
            g.drawImage(subSprite, x, y, null);

            if(this.x <= 0) {
                this.action = Actions.RIGHT;
                this.x = 0;
            }

            recalc();
        }
        else if (this.action == Actions.RIGHT) {
            // Вправо
            x += ENEMY_RUN_SPEED;
            calcReplaceImgWalk();
            subSprite = img.getSubimage(spriteSheetCordsRight[this.subSpritePos][0], spriteSheetCordsRight[this.subSpritePos][1], spriteSheetCordsRight[this.subSpritePos][2], spriteSheetCordsRight[this.subSpritePos][3]);
            g.drawImage(subSprite, x, y, null);

            // достигли конца экрана поворачиваем в обратную сторону
            if(this.x + this.width >= this.gamePane.getWidth()) {
                this.action = Actions.LEFT;
                this.x = this.gamePane.getWidth() - this.width;

            }

            recalc();
        } else if (this.action == Actions.KICKED) {
            if (kickCurrentHeight < kickHeight) {
                kickCurrentHeight += KICK_SPEED_TOP;
                this.y -= KICK_SPEED_TOP;
            }
            else {
                this.action = Actions.KICKED_FALL;
            }

            subSprite = img.getSubimage(spriteSheetCordsHide[this.subSpritePos][0], spriteSheetCordsHide[this.subSpritePos][1], spriteSheetCordsHide[this.subSpritePos][2], spriteSheetCordsHide[this.subSpritePos][3]);
            g.drawImage(subSprite, x, y, null);
        } else if (this.action == Actions.KICKED_FALL) {
            this.y += KICK_SPEED_BOTTOM;
            recalc();

            subSprite = img.getSubimage(spriteSheetCordsHide[this.subSpritePos][0], spriteSheetCordsHide[this.subSpritePos][1], spriteSheetCordsHide[this.subSpritePos][2], spriteSheetCordsHide[this.subSpritePos][3]);
            g.drawImage(subSprite, x, y, null);
        } else if (this.action == Actions.HIDE) {
            if (currentKickTime < kickTime) {
                currentKickTime ++;

                if (currentKickTime > kickTime - (kickTime / 3)) {
                    this.subSpritePos = 0;
                }
            }
            else {
                this.action = (this.gamePane.getRundomNum(0, 1) == 0 ? Actions.LEFT : Actions.RIGHT);
                currentKickTime = 0;
            }

            subSprite = img.getSubimage(spriteSheetCordsHide[this.subSpritePos][0], spriteSheetCordsHide[this.subSpritePos][1], spriteSheetCordsHide[this.subSpritePos][2], spriteSheetCordsHide[this.subSpritePos][3]);
            g.drawImage(subSprite, x, y, null);
        } else if (this.action == Actions.FALL) {
            this.y += ENEMY_FALL_SPEED;

            if (this.actionBeforeFall == Actions.LEFT) {
                subSprite = img.getSubimage(spriteSheetCordsLeft[this.subSpritePos][0], spriteSheetCordsLeft[this.subSpritePos][1], spriteSheetCordsLeft[this.subSpritePos][2], spriteSheetCordsLeft[this.subSpritePos][3]);
                g.drawImage(subSprite, x, y, null);
            } else {
                subSprite = img.getSubimage(spriteSheetCordsRight[this.subSpritePos][0], spriteSheetCordsRight[this.subSpritePos][1], spriteSheetCordsRight[this.subSpritePos][2], spriteSheetCordsRight[this.subSpritePos][3]);
                g.drawImage(subSprite, x, y, null);
            }

            recalc();
        }
        else if (this.action == Actions.KILLED_LEFT) {
            x -= 4;
            y += deathSpeed++;
            subSprite = img.getSubimage(spriteSheetCordsHide[this.subSpritePos][0], spriteSheetCordsHide[this.subSpritePos][1], spriteSheetCordsHide[this.subSpritePos][2], spriteSheetCordsHide[this.subSpritePos][3]);
            g.drawImage(subSprite, x, y, null);

            if (y > gamePane.getHeight()) {
                action = Actions.REMOVE;
            }
        }
        else if (this.action == Actions.KILLED_RIGHT) {
            x += 4;
            y += deathSpeed;
            deathSpeed++;
            subSprite = img.getSubimage(spriteSheetCordsHide[this.subSpritePos][0], spriteSheetCordsHide[this.subSpritePos][1], spriteSheetCordsHide[this.subSpritePos][2], spriteSheetCordsHide[this.subSpritePos][3]);
            g.drawImage(subSprite, x, y, null);

            if (y > gamePane.getHeight()) {
                action = Actions.REMOVE;
            }
        }
    }

    public void setIsKicked(boolean isKicked) {
        this.action = Actions.KICKED;
        this.subSpritePos = 1;
    }

    public void kill(Actions killAction) {
        this.action = killAction;
    }

    public void recalc() {
        // Проверяем юнит находится на одном из блоков, или на нижней точке экрана в противном случае падаем
        boolean onBlock = false;
        for(Block b: this.gamePane.getBlocks()) {
            if ((this.x >= b.getX() && this.x <= b.getX() + b.getWidth() || this.x + this.width >= b.getX() && this.x + this.width <= b.getX() + b.getWidth())
                    && Math.abs(this.y + this.height - b.getY()) <= ENEMY_FALL_SPEED ) {
                onBlock = true;
                if (action == Actions.KICKED_FALL || action == Actions.FALL)  this.y = b.getY() - this.height;
                if(b.getIsKicked()) {
                    setIsKicked(true);
                }
            }
        }

        if (!onBlock && this.y + this.height < this.gamePane.getHeight() && this.action != Actions.FALL && this.action != Actions.KICKED_FALL) {
            this.actionBeforeFall = this.action;
            this.action = Actions.FALL;
        }

        if ((onBlock || this.y + this.height >= this.gamePane.getHeight()) && (this.action == Actions.FALL || this.action == Actions.KICKED_FALL)) {
            if (this.action == Actions.KICKED_FALL) {
                this.action = Actions.HIDE;
            }
            else {
                this.action = (this.gamePane.getRundomNum(0, 1) == 0 ? Actions.LEFT : Actions.RIGHT);
            }
        }

        // заход в зелёные трубы
        if (action == Actions.RIGHT && (x + width/2) >= gamePane.getRightGreenPipe().getLX()
            && getTY() > gamePane.getRightGreenPipe().getTY()) {
            action = Actions.REMOVE;
        }

        if (action == Actions.LEFT && (x + width/2) <= gamePane.getLeftGreenPipe().getRX()
                && getTY() > gamePane.getLeftGreenPipe().getTY()) {
            action = Actions.REMOVE;
        }
    }
}
