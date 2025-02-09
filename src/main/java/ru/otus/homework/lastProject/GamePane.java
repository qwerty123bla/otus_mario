package ru.otus.homework.lastProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePane extends JPanel implements KeyListener, Settings{
    private Hero hero;
    private List<Block> blocks;
    private List<Turtle> turtles;
    private Pipe leftRedPipe;
    private Pipe rightRedPipe;
    private Pipe leftGreenPipe;
    private Pipe rightGreenPipe;
    private Tombstone tombstone;
    private Thread processThread;
    private Random rn;

    private int gameCycleCnt;
    private int turtleCount;
    private int killTurtleCount;

    private int turtleAppearCounter = 200;
    private int currentTurtleAppearCounter;

    private GameLifeCycle gameLifeCycle;

    GamePane() {
        super();
        rn = new Random();
        gameLifeCycle = GameLifeCycle.INIT;
        gameCycleCnt = 0;
        turtleCount = 0;
        killTurtleCount = 0;
    }

    private void init() {
        currentTurtleAppearCounter = 0;
        gameCycleCnt++;

        turtleCount = 0;
        killTurtleCount = 0;

        hero = new Hero(this, "/mario.png", 0, 0);

        blocks = new ArrayList<>();

        blocks.add(new Block(this,0, TOP_BLOCKS_Y));
        blocks.add(new Block(this,50, TOP_BLOCKS_Y));
        blocks.add(new Block(this,100, TOP_BLOCKS_Y));
        blocks.add(new Block(this,150, TOP_BLOCKS_Y));
        blocks.add(new Block(this,200, TOP_BLOCKS_Y));

        blocks.add(new Block(this,1350, TOP_BLOCKS_Y));
        blocks.add(new Block(this,1300, TOP_BLOCKS_Y));
        blocks.add(new Block(this,1250, TOP_BLOCKS_Y));
        blocks.add(new Block(this,1200, TOP_BLOCKS_Y));
        blocks.add(new Block(this,1150, TOP_BLOCKS_Y));

        blocks.add(new Block(this,600, MIDDLE_BLOCKS_Y));
        blocks.add(new Block(this,650, MIDDLE_BLOCKS_Y));
        blocks.add(new Block(this,700, MIDDLE_BLOCKS_Y));
        blocks.add(new Block(this,750, MIDDLE_BLOCKS_Y));

        blocks.add(new Block(this,0, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,50, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,100, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,150, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,200, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,250, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,300, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,350, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,400, BOTTOM_BLOCKS_Y));

        blocks.add(new Block(this,1350, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,1300, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,1250, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,1200, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,1150, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,1100, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,1050, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,1000, BOTTOM_BLOCKS_Y));
        blocks.add(new Block(this,950, BOTTOM_BLOCKS_Y));

        turtles = new ArrayList<>();
        turtles.add(createNewTurtle());

        leftRedPipe = new Pipe(this,"/pipeLeftRed.png", 0, 100, 80, 150);
        rightRedPipe = new Pipe(this, "/pipeRightRed.png", 1320, 100, 80, 150);
        leftGreenPipe = new Pipe(this, "/pipeLeftGreen.png", 0, 600, 150, 150);
        rightGreenPipe = new Pipe(this, "/pipeRightGreen.png", 1250, 600, 150, 150);
    }

    public Turtle createNewTurtle () {
        turtleCount++;
        int pos = getRundomNum(0, 1);
        if (pos == 0) {
            // черепаха появляется слева
            return new Turtle(this,"/turtle.png", 0, TOP_BLOCKS_Y - 100, Actions.RIGHT);
        }
        else {
            // черепаха появляется справа
            return new Turtle(this,"/turtle.png", getWidth() - 70, TOP_BLOCKS_Y - 100, Actions.LEFT);
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Turtle> getTurtles() {
        return turtles;
    }

    public int getRundomNum(int min, int max) {
        return  rn.nextInt(max - min + 1) + min;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameLifeCycle == GameLifeCycle.INIT) {

            System.out.println("h = " + this.getHeight() + " | w = " + this.getWidth());
            init();

            if (gameCycleCnt != 1) {
                gameLifeCycle = GameLifeCycle.RUN;
            }
            else {
                gameLifeCycle = GameLifeCycle.WAIT;
            }
        }

        if (gameLifeCycle == GameLifeCycle.RUN) {
            if (currentTurtleAppearCounter < turtleAppearCounter) {
                currentTurtleAppearCounter++;
            }
            else {
                currentTurtleAppearCounter = 0;
                turtles.add(createNewTurtle());
            }
        }

        if (tombstone != null) {
            tombstone.Draw(g);
        }

        for (Block b: this.blocks) {
            b.Draw(g);
        }

        if (gameLifeCycle == GameLifeCycle.RUN) {
            Iterator<Turtle> turtlesIterator = turtles.iterator();
            while (turtlesIterator.hasNext()) {
                Turtle nextTurtle = turtlesIterator.next();
                if (nextTurtle.getAction() == Actions.REMOVE) {
                    turtlesIterator.remove();
                } else {
                    nextTurtle.Draw(g);
                }
            }
        }

        leftRedPipe.Draw(g);
        rightRedPipe.Draw(g);
        leftGreenPipe.Draw(g);
        rightGreenPipe.Draw(g);

        if (gameLifeCycle == GameLifeCycle.RUN) {
            hero.Draw(g);
        }


        if (gameLifeCycle == GameLifeCycle.WAIT) {
            g.setColor(Color.RED);
            g.setFont(new Font("Times New Roman", Font.BOLD, 30));
            g.drawString("Для начала игры нажмите пробел", 480, 100);
            g.drawString("Управление: стрелки влево/вправо - движение, пробел - прыжок", 260, 150);
        }

        if (gameLifeCycle == GameLifeCycle.END) {
            g.setColor(Color.RED);
            g.setFont(new Font("Times New Roman", Font.BOLD, 30));
            g.drawString("Вы проиграли !!!", 600, 100);
            g.drawString("Черепах появилось " + turtleCount + " черепах убито " + killTurtleCount, 450, 150);
            g.drawString("Для повторного запуска игры нажмите пробел", 400, 200);
        }
    }

    public void play() {
        processThread = new Thread(() -> {
            while(true) {
                this.repaint();
                try {
                    Thread.sleep(20);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        processThread.start();
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public void toLeft() {
        if (gameLifeCycle == GameLifeCycle.RUN) {
            hero.toLeft();
        }
    }

    public void toRight() {
        if (gameLifeCycle == GameLifeCycle.RUN) {
            hero.toRight();
        }
    }

    public void releaseKey() {
        if (gameLifeCycle == GameLifeCycle.RUN) {
            hero.release();
        }
    }

    public void jump() {
        if (gameLifeCycle == GameLifeCycle.WAIT) {
            gameLifeCycle = GameLifeCycle.RUN;
            if (gameCycleCnt != 0)
                destroyTombstone();
        }
        else if (gameLifeCycle == GameLifeCycle.END) {
            gameLifeCycle = GameLifeCycle.INIT;
            if (gameCycleCnt != 0)
                destroyTombstone();
        }
        else if (gameLifeCycle == GameLifeCycle.RUN) {
            hero.jump();
        }
    }

    public Pipe getRightGreenPipe() {
        return rightGreenPipe;
    }

    public Pipe getLeftGreenPipe() {
        return leftGreenPipe;
    }

    public void createTombstone(int x, int y) {
        this.tombstone = new Tombstone(this, x, y);
    }

    public void destroyTombstone() {
        this.tombstone = null;
    }

    public void setGameLifeCycle(GameLifeCycle gameLifeCycle) {
        this.gameLifeCycle = gameLifeCycle;
    }

    public void addKilledTurtle() {
        killTurtleCount++;
    }
}
