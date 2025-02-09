package ru.otus.homework.lastProject;

import java.awt.*;

public class Hero extends Unit {
    private boolean isJump = false;
    private boolean isFall = false;
    private int actionCnt;
    private int currentActionCnt;
    private int subSpritePos;
    private int jumpHeight;
    private int currentJumpHeight;
    private int jumpSpeed;

    Hero(GamePane gamePane, String img, int x, int y) {
        super(gamePane, img, x, y);
        this.x = 10;
        this.y = gamePane.getHeight() - 110;
        isJump = false;
        isFall = false;

        height = 110;
        width = 100;

        action = Actions.STAND;
        actionCnt = RUN_IMG_REPLACE_TIME;
        currentActionCnt = 0;
        subSpritePos = 0;

        jumpHeight = 300;
        currentJumpHeight = 0;

        jumpSpeed = MIN_HERO_JUMP_SPEED;
    }

    public void Draw (Graphics g) {
        if (action == Actions.DEATH) {
            return;
        }

        if(isJump) {
            if (currentJumpHeight < jumpHeight) {
                currentJumpHeight += jumpSpeed; //JUMP_SPEED;
                y -= jumpSpeed; //JUMP_SPEED;
                if (jumpSpeed < MAX_HERO_JUMP_SPEED) jumpSpeed += 1;
            }
            else {
                currentJumpHeight = 0;
                jumpSpeed = MIN_HERO_JUMP_SPEED;
                isJump = false;
                isFall = true;
            }
        }

        if(isFall) {
            y += jumpSpeed; //FALL_SPEED;
            if (jumpSpeed < MAX_HERO_JUMP_SPEED) jumpSpeed += 1;
        }

        boolean onBlock = false;
        boolean underBlock = false;
        boolean rigthBlock = false;
        boolean leftBlock = false;
        for(Block b: this.gamePane.getBlocks()) {
            // не находимся ли мы на блоках или в нижней точке экрана
            if ((this.x >= b.getX() && this.x <= b.getX() + b.getWidth() || this.x + this.width >= b.getX() && this.x + this.width <= b.getX() + b.getWidth())
                    && Math.abs(this.y + this.height - b.getY()) <= jumpSpeed /*FALL_SPEED*/ /*число шага паданеия*/) {
                onBlock = true;
                if (isFall) this.y = b.getY() - this.height;
                break;
            }

            // если при прыжке встречаем сверху блок, то уменьшаем высоту прыжка
            // блоки помечаем как "подброшенные"
            if (((getLX() >= b.getLX() && getLX() <= b.getRX())
                    || (getRX() >= b.getLX() && getRX() <= b.getRX())
                    || (getLX() <= b.getLX() && getRX() >= b.getRX()))
                    && Math.abs(getTY() - (b.getBY())) <= jumpSpeed /*JUMP_SPEED*/) {

                b.setIsKicked(true);
                if (isJump && (currentJumpHeight < jumpHeight - b.getHeight() / 2))
                    currentJumpHeight = jumpHeight - b.getHeight() / 2;
            }

            // проверяем, что блоки не мешают дсижению влево\вправо
            if (action == Actions.RIGHT && Math.abs(getRX() - b.getLX()) <= RUN_SPEED
                    && ((getTY() <= b.getTY() && getBY() >= b.getBY()) || (getTY() >= b.getTY() && getTY() <= b.getBY()) || (getBY() >= b.getTY() && getBY() <= b.getBY()))) {
                rigthBlock = true;
            }

            if (action == Actions.LEFT && Math.abs(getLX() - b.getRX()) <= RUN_SPEED
                    && ((getTY() <= b.getTY() && getBY() >= b.getBY()) || (getTY() >= b.getTY() && getTY() <= b.getBY()) || (getBY() >= b.getTY() && getBY() <= b.getBY()))) {
                leftBlock = true;
            }
        }

        if (!onBlock && this.y + this.height < this.gamePane.getHeight() && !isFall && !isJump) {
            isFall = true;
        }

        if ((onBlock || this.y + this.height >= this.gamePane.getHeight()) && isFall) {
            isFall = false;
            jumpSpeed = MIN_HERO_JUMP_SPEED;
            if (this.y + this.height >= this.gamePane.getHeight())  y = gamePane.getHeight() - height;
        }

        if (rigthBlock || getRX() >= gamePane.getWidth()) {
            x -= RUN_SPEED;
        }

        if (leftBlock || getLX() <= 0) {
            x += RUN_SPEED;
        }

        // пересечение с черепахами
        for(Turtle b: this.gamePane.getTurtles()) {
            if( getLX() <= b.getRX() &&
                    getRX() >= b.getLX() &&
                    getTY() <= b.getBY() &&
                    getBY() >= b.getTY() &&
                    b.getAction() != Actions.KILLED_LEFT &&
                    b.getAction() != Actions.KILLED_RIGHT
            ) {
                if ((action == Actions.RIGHT || action == Actions.STAND) && b.getAction() == Actions.HIDE) {
                    b.kill(Actions.KILLED_RIGHT);
                    gamePane.addKilledTurtle();
                }
                else if (action == Actions.LEFT && b.getAction() == Actions.HIDE) {
                    b.kill(Actions.KILLED_LEFT);
                    gamePane.addKilledTurtle();
                }
                else {
                    gamePane.createTombstone(x, y);
                    action = Actions.DEATH;
                }
            }
        }
        // -------------------------------------------------------------------------------------------------------------

        Image subSprite;

        int[][] spriteSheetCordsRunRight = { { 100, 0, 100, 110 }, { 200, 0, 100, 110 } };
        int[][] spriteSheetCordsRunLeft = { { 400, 0, 100, 110 }, { 500, 0, 100, 110 } };
        int[][] spriteSheetCordsStand = { { 0, 0, 100, 110 }};
        int[][] spriteSheetCordsJumpRight = { { 300, 0, 100, 110 }};
        int[][] spriteSheetCordsJumpLeft = { { 600, 0, 100, 110 }};

        if (action == Actions.STAND) {
            if (isJump || isFall) {
                subSprite = img.getSubimage(spriteSheetCordsJumpRight[0][0], spriteSheetCordsJumpRight[0][1], spriteSheetCordsJumpRight[0][2], spriteSheetCordsJumpRight[0][3]);
            }
            else {
                subSprite = img.getSubimage(spriteSheetCordsStand[0][0], spriteSheetCordsStand[0][1], spriteSheetCordsStand[0][2], spriteSheetCordsStand[0][3]);
            }

            g.drawImage(subSprite, x, y, null);
        }
        else if (action == Actions.RIGHT) {
            x += RUN_SPEED;
            if (this.currentActionCnt < this.actionCnt) {
                this.currentActionCnt++;
            }
            else  {
                this.currentActionCnt = 0;
                if (this.subSpritePos == 0) this.subSpritePos = 1; else this.subSpritePos = 0;
            }

            if (isJump || isFall) {
                subSprite = img.getSubimage(spriteSheetCordsJumpRight[0][0], spriteSheetCordsJumpRight[0][1], spriteSheetCordsJumpRight[0][2], spriteSheetCordsJumpRight[0][3]);
            }
            else {
                subSprite = img.getSubimage(spriteSheetCordsRunRight[this.subSpritePos][0], spriteSheetCordsRunRight[this.subSpritePos][1], spriteSheetCordsRunRight[this.subSpritePos][2], spriteSheetCordsRunRight[this.subSpritePos][3]);
            }
            g.drawImage(subSprite, x, y, null);
        }
        else if (action == Actions.LEFT) {
            x -= RUN_SPEED;
            if (this.currentActionCnt < this.actionCnt) {
                this.currentActionCnt++;
            }
            else  {
                this.currentActionCnt = 0;
                if (this.subSpritePos == 0) this.subSpritePos = 1; else this.subSpritePos = 0;
            }

            if (isJump || isFall) {
                subSprite = img.getSubimage(spriteSheetCordsJumpLeft[0][0], spriteSheetCordsJumpLeft[0][1], spriteSheetCordsJumpLeft[0][2], spriteSheetCordsJumpLeft[0][3]);
            }
            else {
                subSprite = img.getSubimage(spriteSheetCordsRunLeft[this.subSpritePos][0], spriteSheetCordsRunLeft[this.subSpritePos][1], spriteSheetCordsRunLeft[this.subSpritePos][2], spriteSheetCordsRunLeft[this.subSpritePos][3]);
            }

            g.drawImage(subSprite, x, y, null);
        }
    }

    public void toRight() {
        if (action == Actions.DEATH) {
            return;
        }

        action = Actions.RIGHT;
    }

    public void toLeft() {
        if (action == Actions.DEATH) {
            return;
        }

        action = Actions.LEFT;
    }

    public void release() {
        if (action == Actions.DEATH) {
            return;
        }

        if (action == Actions.RIGHT || action == Actions.LEFT) {
            action = Actions.STAND;
        }
    }

    public void jump() {
        if (action == Actions.DEATH) {
            return;
        }

        if (!isFall && !isJump) {
            isJump = true;
            jumpSpeed = 2;
        }
    }

    public int getLX() {
        return x + (width / 6);
    }

    public int getRX() {
        return x + width - (width / 6);
    }

    public int getTY() {
        return y + (height / 8);
    }

    public int getBY() {
        return y + height - (height / 8);
    }
}
