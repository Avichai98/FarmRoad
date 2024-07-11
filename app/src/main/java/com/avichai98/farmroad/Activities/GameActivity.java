package com.avichai98.farmroad.Activities;

import static com.avichai98.farmroad.Manager.GameManager.COLS;
import static com.avichai98.farmroad.Manager.GameManager.COW;
import static com.avichai98.farmroad.Manager.GameManager.DELAY;
import static com.avichai98.farmroad.Manager.GameManager.EMPTY;
import static com.avichai98.farmroad.Manager.GameManager.HAY;
import static com.avichai98.farmroad.Manager.GameManager.ROWS;
import android.content.Intent;
import android.os.Bundle;
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
import com.avichai98.farmroad.Manager.GameManager;
import com.avichai98.farmroad.Interfaces.MoveCallback;
import com.avichai98.farmroad.R;
import com.avichai98.farmroad.Utilities.MoveDetector;
import com.avichai98.farmroad.Utilities.PlaySound;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import java.lang.reflect.Field;

public class GameActivity extends AppCompatActivity {

    private AppCompatImageView[][] game_IMG_objects;
    private AppCompatImageView[] game_IMG_hearts;
    private int game_IMG_hay;
    private int game_IMG_cow;
    private MaterialTextView game_LBL_score;
    private MaterialButton game_BTN_left;
    private MaterialButton game_BTN_right;
    private GameManager gameManager;
    private PlaySound playSound;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, DELAY);
            tick();
        }
    };
    private MoveDetector moveDetector;
    private double latitude;
    private double longitude;
    private boolean isFast;
    private boolean isArrows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameManager = new GameManager();
        playSound = new PlaySound(this);
        findViews();
        initIntent();

        if(isFast)
            speedUp();
        else
            speedDown();

        if(!isArrows)
            initMoveDetector();
        updateTractorUI();
        updateObjectsUi();

        game_BTN_right.setOnClickListener(View -> rightMove());
        game_BTN_left.setOnClickListener(View -> leftMove());
    }

    private void initIntent() {
        Intent prev = getIntent();
        Bundle bundle = prev.getExtras();
        assert bundle != null;
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        isFast = bundle.getBoolean("isFast");
        isArrows = bundle.getBoolean("isArrows");
    }

    private void initMoveDetector() {
        game_BTN_right.setVisibility(View.INVISIBLE);
        game_BTN_left.setVisibility(View.INVISIBLE);

        moveDetector = new MoveDetector(this, new MoveCallback() {
            @Override
            public void moveRightX() {
                rightMove();
            }
            @Override
            public void moveLeftX() {
                leftMove();
            }

            @Override
            public void speedUpZ() {
                speedUp();
            }

            @Override
            public void speedDownZ() {
                speedDown();
            }
        });

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
        if(!isArrows)
            moveDetector.start();
        handler.postDelayed(runnable, DELAY);
    }

    @Override
    protected void onPause() {
        Log.d("g", "onPause");
        if(!isArrows)
            moveDetector.stop();
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

    private void speedUp(){
        DELAY = 600;
    }

    private void speedDown(){
        DELAY = 1000;
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
                int id = getResId(baseName, i, j);
                game_IMG_objects[i][j] = findViewById(id);
            }
            if (i == ROWS - 2)
                baseName = "game_IMG_tractor";
        }

        game_IMG_hay = R.drawable.ic_hay;
        game_IMG_cow = R.drawable.cow_img;
    }

    private int getResId(String baseName, int i, int j) {
        try {
            Field idField = R.id.class.getDeclaredField(baseName + i + j);
            return idField.getInt(idField);
        } catch (Exception e) {
            Log.e("g", "getResId error: " + e.getMessage());
            return -1;
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
    private void updateObjectsUi() {
        for (int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLS; j++) {
                if (gameManager.getObjects()[i][j] == EMPTY)
                    game_IMG_objects[i][j].setVisibility(View.INVISIBLE);
                else if (gameManager.getObjects()[i][j] == HAY) {
                    game_IMG_objects[i][j].setImageResource(game_IMG_hay);
                    game_IMG_objects[i][j].setVisibility(View.VISIBLE);
                }
                else if (gameManager.getObjects()[i][j] == COW) {
                    game_IMG_objects[i][j].setImageResource(game_IMG_cow);
                    game_IMG_objects[i][j].setVisibility(View.VISIBLE);
                }
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
        moveDetector.start();
        handler.postDelayed(runnable, DELAY);
    }
    private void lose() {
        Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show();
        gameDone();
    }

    private void gameDone() {
        gameManager.saveRecord(latitude, longitude);
        Intent intent = new Intent(GameActivity.this, RecordsActivity.class);
        startActivity(intent);
        Log.d("g", "Game Done");
        game_BTN_right.setEnabled(false);
        game_BTN_left.setEnabled(false);
        finish();
    }

    private void vibrate(int milliseconds){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliseconds);
        }
    }
    private void tick() {
        int currentScore = gameManager.getScore();
        gameManager.tick();
        updateObjectsUi();
        updateLivesUI();
        if (gameManager.getCollision() == 1){
            vibrate(500);

            if(gameManager.getLives() == 0) {
                playSound.playSound(R.raw.cow_moo_end_game);
                if(!isArrows)
                    moveDetector.stop();
                openAdvertisementDialog();
                return;
            }
            playSound.playSound(R.raw.cow_breath_collision);
            Toast.makeText(this, "You killed a cow!", Toast.LENGTH_SHORT).show();
        } else if (gameManager.getCollision() == 2) {
            vibrate(1000);
            playSound.playSound(R.raw.boost_score);
            Toast.makeText(this, "Good job!\nYou got hay!", Toast.LENGTH_SHORT).show();
        }
        if (currentScore < gameManager.getScore()) {
            game_LBL_score.setText(String.valueOf(gameManager.getScore()));
            playSound.playSound(R.raw.cowbell_add_score);
        }
    }
}