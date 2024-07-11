package com.avichai98.farmroad.Manager;

import com.avichai98.farmroad.Utilities.MSPV;
import com.google.gson.Gson;
import java.util.Random;

public class GameManager {
    public static final String RECORDSLIST = "RECORDSLIST";
    public static int DELAY = 1000;
    public static final int ROWS = 6;
    public static final int COLS = 5;
    public static final int EMPTY = 0;
    public static final int COW = 1;
    public static final int TRACTOR = 2;
    public  static final int HAY = 3;
    private int lives;
    private int score;
    private int tractorIndex;
    private int rndCowIndex;
    private int rndHayIndex;
    private int tickNumber;
    private final int[][] objects;
    private int isCollision;
    private final Record record;
    private final Gson gson;


    public GameManager() {
        this.lives = 3;
        this.score = 0;
        this.tickNumber = 0;
        this.rndCowIndex = 0;
        this.rndHayIndex = 0;
        this.objects = new int[ROWS][COLS];
        this.objects[ROWS- 1][COLS / 2] = TRACTOR; //Insert the tractor at the middle
        this.tractorIndex = COLS / 2;
        this.isCollision = 0;
        this.record = new Record(0);
        this.gson = new Gson();
    }

    public void incrementScore(int score) {
        this.score += score;
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

    public int getCollision() {
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
        checkCollision();

        for (int i = ROWS - 2; i > 0; i--) {
            for (int j = 0; j < COLS; j++) {
                this.objects[i][j] = this.objects[i - 1][j];
                this.objects[i - 1][j] = EMPTY;
            }
        }
         do{
            this.rndCowIndex = getRandom();
            this.rndHayIndex = getRandom();
        }while (this.rndCowIndex == this.rndHayIndex);
        this.objects[0][rndCowIndex] = COW;

        if(this.tickNumber % 2 == 1)
            this.objects[0][rndHayIndex] = HAY;

        this.tickNumber++;
    }

    private void checkCollision() {
        if(this.objects[ROWS - 2][tractorIndex] == COW) {
            isCollision = 1; //1 means collision with cow
            decreaseLive();
        }
        else if (this.objects[ROWS - 2][tractorIndex] == HAY){
            isCollision = 2; //2 means collision with hay
            incrementScore(50);
        }
        else{
            isCollision = 0;//0 means no collision
            for(int j = 0; j < COLS; j++){
                if(this.objects[ROWS - 2][j] == COW)
                    incrementScore(10);
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

    public void saveRecord(double lat, double lon) {
        //Set location
        record.setLat(lat);
        record.setLon(lon);
        record.setScore(this.score);
        RecordsManager.getInstance().addRecord(record);

        //Save to data base
        String toJson = gson.toJson(RecordsManager.getInstance());
        MSPV.getInstance().saveString(RECORDSLIST,toJson);
    }
}
