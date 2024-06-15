package com.avichai98.farmroad;

import java.util.Random;

public class GameManager {
    public static final int DELAY = 1300;
    public static final int ROWS = 6;
    public static final int COLS = 3;
    public static final int EMPTY = 0;
    public static final int COW = 1;
    public static final int TRACTOR = 2;
    private int lives;
    private int score;
    private int tractorIndex;

    private final int[][] objects;
    private boolean isCollision;


    public GameManager() {
        this.lives = 3;
        this.score = 0;
        this.objects = new int[ROWS][COLS];
        this.objects[ROWS- 1][COLS / 2] = TRACTOR; //Insert the tractor at the middle
        this.tractorIndex = COLS / 2;
        this.isCollision = false;
    }

    public void incrementScore() {
        score += 10;
    }

    public void decreaseLive() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        if(lives <= 3 && lives >= 0)
            this.lives = lives;
        else
            this.lives = 3;
    }

    public int getScore() {
        return score;
    }

    public int getTractorIndex() {
        return tractorIndex;
    }

    public boolean getCollision() {
        return isCollision;
    }
    private int getRandom() {
        Random rnd = new Random();
        return rnd.nextInt(COLS);
    }
    public int[][] getObjects() {
        return objects;
    }

    public void tick() {
        int rndIndex;

        checkCollision();

        for (int i = ROWS - 2; i > 0; i--) {
            for (int j = 0; j < COLS; j++) {
                this.objects[i][j] = this.objects[i - 1][j];
                this.objects[i - 1][j] = EMPTY;
            }
        }
        rndIndex = getRandom();
        this.objects[0][rndIndex] = COW;
    }

    private void checkCollision() {
        if(this.objects[ROWS - 2][tractorIndex] == COW) {
            isCollision = true;
            decreaseLive();
        }
        else{
            isCollision = false;
            for(int j = 0; j < COLS; j++){
                if(this.objects[ROWS - 2][j] == COW)
                    incrementScore();
            }
        }
    }

    public void moveTractorLeft() {
        this.objects[ROWS- 1][tractorIndex] = EMPTY;
        this.objects[ROWS- 1][tractorIndex - 1] = TRACTOR;
        this.tractorIndex -= 1;
    }

    public void moveTractorRight() {
        this.objects[ROWS- 1][tractorIndex] = EMPTY;
        this.objects[ROWS- 1][tractorIndex + 1] = TRACTOR;
        this.tractorIndex += 1;
    }
}
