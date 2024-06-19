package com.avichai98.farmroad;

import static com.avichai98.farmroad.GameManager.COLS;
import static com.avichai98.farmroad.GameManager.DELAY;
import static com.avichai98.farmroad.GameManager.EMPTY;
import static com.avichai98.farmroad.GameManager.ROWS;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;


public class GameActivity extends AppCompatActivity {

    private AppCompatImageView[][] game_IMG_objects;
    private AppCompatImageView[] game_IMG_hearts;
    private MaterialTextView game_LBL_score;
    private MaterialButton game_BTN_left;
    private MaterialButton game_BTN_right;
    private GameManager gameManager;
    private PlaySound playSound;
    private int currentScore;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, DELAY);
            tick();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        gameManager = new GameManager();
        playSound = new PlaySound(this);
        findViews();
        updateTractorUI();
        updateCowUi();

        game_BTN_right.setOnClickListener(View -> rightMove());
        game_BTN_left.setOnClickListener(View -> leftMove());
    }

    @Override
    protected void onStart() {
        Log.d("g", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("g", "onResume");
        super.onResume();
        handler.postDelayed(runnable, DELAY);
    }

    @Override
    protected void onPause() {
        Log.d("g", "onPause");
        super.onPause();
    }


    @Override
    protected void onStop() {
        Log.d("g", "onStop");
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        Log.d("g", "onDestroy");
        super.onDestroy();
    }

    private void leftMove() {
        if(gameManager.getTractorIndex() != 0){
            gameManager.moveTractorLeft();
            updateTractorUI();
        }
    }

    private void rightMove() {
        if(gameManager.getTractorIndex() != COLS -1){
            gameManager.moveTractorRight();
            updateTractorUI();
        }
    }

    private void findViews() {
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_BTN_right = findViewById(R.id.game_BTN_right);

        game_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
        };

        game_IMG_objects = new AppCompatImageView[ROWS][COLS];
        String baseName = "game_IMG_cow";
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int id = getResources().getIdentifier(baseName + i + j, "id", getPackageName());
                game_IMG_objects[i][j] = findViewById(id);
            }
            if (i == ROWS - 2)
                baseName = "game_IMG_tractor";
        }
    }

    private void updateTractorUI(){
        for (int j = 0; j < COLS; j++) {
            if(gameManager.getObjects()[ROWS - 1][j] == 0)
                game_IMG_objects[ROWS - 1][j].setVisibility(View.INVISIBLE);
            else
                game_IMG_objects[ROWS - 1][j].setVisibility(View.VISIBLE);
        }
    }
    private void updateCowUi() {
        for (int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLS; j++) {
                if (gameManager.getObjects()[i][j] == EMPTY)
                    game_IMG_objects[i][j].setVisibility(View.INVISIBLE);
                else
                    game_IMG_objects[i][j].setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateLivesUI() {
        int SZ = game_IMG_hearts.length;

        for (AppCompatImageView triviaImgHeart : game_IMG_hearts) {
            triviaImgHeart.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < SZ - gameManager.getLives(); i++) {
            game_IMG_hearts[SZ - i - 1].setVisibility(View.INVISIBLE);
        }
    }

    private void openAdvertisementDialog() {
        handler.removeCallbacks(runnable);
        new MaterialAlertDialogBuilder(this)
                .setTitle("No lives")
                .setMessage("Do you want to continue?")
                .setPositiveButton("Yes", (dialog, which) -> continueTheGame())
                .setNegativeButton("No", (dialog, which) -> lose())
                .setCancelable(false)
                .show();
    }

    private void continueTheGame(){
        gameManager.setLives(3);
        updateLivesUI();
        handler.postDelayed(runnable, DELAY);
    }
    private void lose() {
        Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show();
        gameDone();
    }

    private void gameDone() {
        Log.d("g", "Game Done");
        game_BTN_right.setEnabled(false);
        game_BTN_left.setEnabled(false);
        finish();
    }

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
    private void tick() {
        this.currentScore = gameManager.getScore();
        gameManager.tick();
        updateCowUi();
        updateLivesUI();
        if (gameManager.getCollision()){
            vibrate();

            if(gameManager.getLives() == 0) {
                playSound.playSound(R.raw.cow_moo_end_game);
                openAdvertisementDialog();
                return;
            }
            playSound.playSound(R.raw.cow_breath_collision);
            Toast.makeText(this, "You killed a cow!", Toast.LENGTH_SHORT).show();
        }
        if (this.currentScore < gameManager.getScore()) {
            game_LBL_score.setText(String.valueOf(gameManager.getScore()));
            playSound.playSound(R.raw.cowbell_add_score);
        }
    }
}