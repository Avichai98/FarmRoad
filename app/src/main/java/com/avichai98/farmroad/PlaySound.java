package com.avichai98.farmroad;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlaySound {
    private final Context context;
    private final Executor executor;
    private MediaPlayer mediaPlayer;


    public PlaySound(Context context){
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void playSound(int resId){
        executor.execute(() ->{
            mediaPlayer = MediaPlayer.create(context, resId);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        });
    }
}
