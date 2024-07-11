package com.avichai98.farmroad.Utilities;

import android.content.Context;
import android.media.MediaPlayer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlaySound {
    private final Context context;
    private final Executor executor;


    public PlaySound(Context context) {
        this.context = context.getApplicationContext();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void playSound(int resId) {
        executor.execute(() -> {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, resId);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.stop();
                mp.release();
                mp = null;
            });

        });

    }
}
